package com.lifengqiang.video.camera;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.view.Surface;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.util.Arrays;

public class Camera2Wrapper {
    private final String TAG = "Camera2Wrapper";
    private final Size DEFAULT_PREVIEW_SIZE = new Size(1920, 1080);
    private final Size DEFAULT_CAPTURE_SIZE = new Size(1920, 1080);

    private final Context mContext;
    private final CameraManager mCameraManager;
    private CameraDevice mCameraDevice;
    private CameraCaptureSession mCameraCaptureSession;
    private String mCameraId, mFrontCameraId, mBackCameraId;
    private Size mPreviewSize, mPictureSize;

    private ImageReader mPreviewImageReader, mCaptureImageReader;
    private int mSensorOrientation;

    private Surface mPreviewSurface;

    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;

    private FrameCallback mFrameCallback;

    public Camera2Wrapper(Context context) {
        mContext = context;
        mCameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        mFrameCallback = (FrameCallback) context;
        initCamera();
        useBackCamera();
    }

    private ImageReader.OnImageAvailableListener mOnPreviewImageAvailableListener = reader -> {
        Image image = reader.acquireLatestImage();
        if (image != null) {
            if (mFrameCallback != null) {
                mFrameCallback.onVideoFrame(image);
            }
            image.close();
        }
    };

    private ImageReader.OnImageAvailableListener mOnCaptureImageAvailableListener = reader -> {
        Image image = reader.acquireLatestImage();
        if (image != null) {
            if (mFrameCallback != null) {
                mFrameCallback.onImageFrame(image);
            }
            image.close();
        }
    };

    private void initCamera() {
        try {
            String[] cameraIdList = mCameraManager.getCameraIdList();
            for (String cameraId : cameraIdList) {
                CameraCharacteristics characteristics = mCameraManager.getCameraCharacteristics(cameraId);
                Integer orientation = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (orientation == null) continue;
                if (orientation == CameraCharacteristics.LENS_FACING_FRONT) {
                    mFrontCameraId = cameraId;
                } else if (orientation == CameraCharacteristics.LENS_FACING_BACK) {
                    mBackCameraId = cameraId;
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private boolean getCameraSizeParameter() {
        try {
            CameraCharacteristics characteristics = mCameraManager.getCameraCharacteristics(mCameraId);
            mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            StreamConfigurationMap streamConfigs = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            Size[] sizes = streamConfigs.getOutputSizes(ImageReader.class);
            mPreviewSize = null;
            mPictureSize = null;
            for (Size size : sizes) {
                if (size.equals(DEFAULT_PREVIEW_SIZE)) {
                    mPreviewSize = new Size(size.getWidth(), size.getHeight());
                }
                if (size.equals(DEFAULT_CAPTURE_SIZE)) {
                    mPictureSize = new Size(size.getWidth(), size.getHeight());
                }
            }
            return mPreviewSize != null && mPictureSize != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void useBackCamera() {
        mCameraId = mBackCameraId;
    }

    public void useFrontCamera() {
        mCameraId = mFrontCameraId;
    }

    public void toggleCamera() {
        if (mCameraId == null) {
            mCameraId = mBackCameraId;
        } else if (mCameraId.equals(mBackCameraId)) {
            mCameraId = mFrontCameraId;
        } else if (mCameraId.equals(mFrontCameraId)) {
            mCameraId = mBackCameraId;
        }
    }

    public void startCamera() {
        stopCamera();
        if (getCameraSizeParameter()) {
            startBackgroundThread();
            if (mPreviewImageReader == null && mPreviewSize != null) {
                mPreviewImageReader = ImageReader.newInstance(mPreviewSize.getWidth(), mPreviewSize.getHeight(), ImageFormat.YUV_420_888, 2);
                mPreviewImageReader.setOnImageAvailableListener(mOnPreviewImageAvailableListener, mBackgroundHandler);
                mPreviewSurface = mPreviewImageReader.getSurface();
            }
            if (mCaptureImageReader == null && mPictureSize != null) {
                mCaptureImageReader = ImageReader.newInstance(mPictureSize.getWidth(), mPictureSize.getHeight(), ImageFormat.YUV_420_888, 2);
                mCaptureImageReader.setOnImageAvailableListener(mOnCaptureImageAvailableListener, mBackgroundHandler);
            }
            openCamera();
        } else {
            if (mFrameCallback != null) {
                mFrameCallback.onShowToast("此相机不具有合适的预览尺寸");
            }
        }
    }

    public void stopCamera() {
        if (mPreviewImageReader != null) {
            mPreviewImageReader.setOnImageAvailableListener(null, null);
        }

        if (mCaptureImageReader != null) {
            mCaptureImageReader.setOnImageAvailableListener(null, null);
        }
        closeCamera();
        stopBackgroundThread();
    }

    private void openCamera() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        try {
            mCameraManager.openCamera(mCameraId, mStateCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void closeCamera() {
        if (null != mCameraCaptureSession) {
            mCameraCaptureSession.close();
            mCameraCaptureSession = null;
        }
        if (null != mCameraDevice) {
            mCameraDevice.close();
            mCameraDevice = null;
        }
        if (null != mPreviewImageReader) {
            mPreviewImageReader.close();
            mPreviewImageReader = null;
        }

        if (null != mCaptureImageReader) {
            mCaptureImageReader.close();
            mCaptureImageReader = null;
        }
    }

    private final CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            // This method is called when the camera is opened.  We start camera preview here.
            mCameraDevice = cameraDevice;
            createCaptureSession();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            cameraDevice.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int error) {
            cameraDevice.close();
            mCameraDevice = null;
        }
    };

    private void createCaptureSession() {
        try {
            if (null == mCameraDevice || null == mPreviewSurface || null == mCaptureImageReader)
                return;
            mCameraDevice.createCaptureSession(Arrays.asList(mPreviewSurface, mCaptureImageReader.getSurface()),
                    mSessionStateCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            Log.e(TAG, "createCaptureSession " + e.toString());
        }
    }

    private final CameraCaptureSession.StateCallback mSessionStateCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession session) {
            mCameraCaptureSession = session;
            try {
                CaptureRequest.Builder builder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
                builder.addTarget(mPreviewImageReader.getSurface());
                if (mPreviewSurface != null) {
                    builder.addTarget(mPreviewSurface);
                }
                CaptureRequest request = builder.build();
                session.setRepeatingRequest(request, null, mBackgroundHandler);
                if (mFrameCallback != null) {
                    mFrameCallback.onPreviewStart();
                }
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
            Log.e(TAG, "onConfigureFailed");
        }
    };

    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera2Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    private void stopBackgroundThread() {
        if (mBackgroundThread != null) {
            mBackgroundThread.quitSafely();
            try {
                mBackgroundThread.join();
                mBackgroundThread = null;
                mBackgroundHandler = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Size getDefaultPreviewSize() {
        return DEFAULT_PREVIEW_SIZE;
    }

    public Size getDefaultPictureSize() {
        return DEFAULT_CAPTURE_SIZE;
    }

    public int getAngle() {
        return mSensorOrientation;
    }

    public String getCameraId() {
        return mCameraId;
    }

    public void release() {
        stopCamera();
    }

    public interface FrameCallback {
        void onPreviewStart();

        void onVideoFrame(Image image);

        void onImageFrame(Image image);

        void onShowToast(String message);
    }
}
