package com.lifengqiang.video.ui.create.record;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.camera2.CameraCharacteristics;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.lifengqiang.video.R;
import com.lifengqiang.video.base.activity.FullScreenActivity;
import com.lifengqiang.video.camera.AudioRecorder;
import com.lifengqiang.video.camera.Camera2Wrapper;
import com.lifengqiang.video.camera.CameraControllerView;
import com.lifengqiang.video.camera.CameraPreviewView;
import com.lifengqiang.video.jni.encoder.FFMediaEncoder;
import com.lifengqiang.video.jni.renderer.CameraPreviewRenderer;
import com.lifengqiang.video.jni.renderer.NativeImage;
import com.lifengqiang.video.ui.create.photo.image.PhotoImageActivity;
import com.lifengqiang.video.ui.create.photo.video.PhotoVideoActivity;
import com.lifengqiang.video.ui.create.submit.image.SubmitImageActivity;
import com.lifengqiang.video.ui.create.submit.video.SubmitVideoActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.UUID;

public class RecordActivity extends FullScreenActivity<RecordView> implements
        Camera2Wrapper.FrameCallback, AudioRecorder.AudioRecorderCallback, CameraPreviewView.OnFrameRenderCallback {
    public static final int START_MODE_VIDEO = 0;
    public static final int START_MODE_IMAGE = 1;

    public static final int REQUEST_VIDEO = 2;
    public static final int REQUEST_IMAGE = 3;

    private Camera2Wrapper camera;
    private AudioRecorder audioRecorder;
    private FFMediaEncoder encoder;
    private File outputFile;

    public static void start(Context context, int mode) {
        Intent intent = new Intent(context, RecordActivity.class);
        intent.putExtra("mode", mode);
        context.startActivity(intent);
    }

    public static void startVideo(Context context) {
        start(context, START_MODE_VIDEO);
    }

    public static void startImage(Context context) {
        start(context, START_MODE_IMAGE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        setStatusBar(true);
        click(R.id.back, this::finish);
        CameraControllerView view = get(R.id.camera);
        view.setCallback(new CameraControllerView.Callback() {
            @Override
            public void onSelectRecord(CameraControllerView view) {
            }

            @Override
            public void onSelectPicture(CameraControllerView view) {
            }

            @Override
            public void onStartRecord(CameraControllerView view) {
                view.start();
                audioRecorder = new AudioRecorder(RecordActivity.this);
                audioRecorder.start();
                String fileName = UUID.randomUUID().toString() + ".mp4";
                outputFile = new File(getExternalFilesDir("/record/video"), fileName);
                encoder = new FFMediaEncoder();
                encoder.startEncode(outputFile.getAbsolutePath());
            }

            @Override
            public void onStopRecord(CameraControllerView view) {
                view.stop();
                if (audioRecorder != null) {
                    audioRecorder.interrupt();
                }
                if (encoder != null) {
                    AlertDialog dialog = new AlertDialog.Builder(getContext())
                            .setView(new ProgressBar(getContext()))
                            .setMessage("等待中")
                            .setCancelable(false)
                            .show();
                    new Thread(() -> {
                        encoder.stopEncode();
                        encoder.releaseNativeObject();
                        encoder = null;
                        mainHandler.post(() -> {
                            SubmitVideoActivity.start(getContext(), outputFile);
                            dialog.dismiss();
                        });
                    }).start();
                }
            }

            @Override
            public void onTakePicture(CameraControllerView view) {
                takePicture();
            }

            @Override
            public void onToggleCamera(CameraControllerView view) {
                getCamera().toggleCamera();
                getCamera().startCamera();
            }

            @Override
            public void onOpenPhotoAlbum(CameraControllerView view) {
                View layout = View.inflate(getContext(), R.layout.view_photo_mode, null);
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setView(layout)
                        .show();
                layout.findViewById(R.id.image).setOnClickListener((v) -> {
                    startActivityForResult(new Intent(getContext(), PhotoImageActivity.class), REQUEST_IMAGE);
                    dialog.dismiss();
                });
                layout.findViewById(R.id.video).setOnClickListener((v) -> {
                    startActivityForResult(new Intent(getContext(), PhotoVideoActivity.class), REQUEST_VIDEO);
                    dialog.dismiss();
                });
            }
        });
        getCamera().startCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        view.getPreviewView().onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        view.getPreviewView().onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getCamera().release();
        camera = null;
    }

    public Camera2Wrapper getCamera() {
        synchronized (Camera2Wrapper.class) {
            if (camera == null) {
                camera = new Camera2Wrapper(this);
            }
            return camera;
        }
    }

    @Override
    public void onPreviewStart() {
        Camera2Wrapper camera = getCamera();
        CameraPreviewView previewView = view.getPreviewView();
        previewView.setCanvas(camera.getDefaultPreviewSize(), camera.getAngle());
        if (Integer.parseInt(camera.getCameraId()) == CameraCharacteristics.LENS_FACING_FRONT) {
            previewView.setMirror(0);
        } else {
            previewView.setMirror(1);
        }
        previewView.setCallback(this);
    }

    @Override
    public void onVideoFrame(Image image) {
        view.getPreviewView().renderImage(image);
    }

    @Override
    public void onImageFrame(Image image) {
        // 使用takePicture方法
    }

    @Override
    public void onShowToast(String message) {
        toast(message);
    }

    @Override
    public void onAudioData(byte[] data, int dataSize) {
        if (encoder != null) {
            encoder.onAudioData(data, dataSize);
        }
    }

    @Override
    public void onError(String msg) {
    }

    @Override
    public void onRendererFrame(NativeImage image) {
        if (encoder != null) {
            encoder.onVideoData(image);
        }
    }

    private void takePicture() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setView(new ProgressBar(this))
                .show();
        new Thread(() -> {
            CameraPreviewRenderer renderer = view.getPreviewView().getRenderer();
            NativeImage nativeImage = renderer.getRendererFrameBuffer();
            ByteBuffer buffer = nativeImage.getBuffer();
            Bitmap bitmap = Bitmap.createBitmap(nativeImage.getWidth(), nativeImage.getHeight(), Bitmap.Config.ARGB_8888);
            bitmap.copyPixelsFromBuffer(buffer);
            String fileName = UUID.randomUUID().toString() + ".jpg";
            File file = new File(getExternalFilesDir("/record/picture"), fileName);
            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, new FileOutputStream(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            mainHandler.post(() -> {
                dialog.dismiss();
                toast("已保存到相册");
            });
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_VIDEO && data != null) {
                File file = (File) data.getSerializableExtra(PhotoVideoActivity.VIDEO_FILE_PATH);
                if (file == null) {
                    toast("没有视频");
                } else {
                    SubmitVideoActivity.start(this, file);
                }
            } else if (requestCode == REQUEST_IMAGE && data != null) {
                ArrayList<File> files = (ArrayList<File>) data.getSerializableExtra(PhotoImageActivity.IMAGE_FILES_PATH);
                if (files == null || files.isEmpty()) {
                    toast("请选择至少一张图片");
                } else {
                    SubmitImageActivity.start(this, files);
                }
            }
        }
    }
}
