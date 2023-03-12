package com.lifengqiang.video.ui.create.submit.video;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Size;
import android.view.Surface;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.lifengqiang.video.R;
import com.lifengqiang.video.api.Api;
import com.lifengqiang.video.base.activity.CaptionedActivity;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class SubmitVideoActivity extends CaptionedActivity<SubmitVideoView> {
    public Thread thread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPageTitle("视频发布");
        setContentView(R.layout.activity_submit_video);
        click(R.id.post, () -> {
            AlertDialog dialog = new AlertDialog.Builder(getContext())
                    .setView(new ProgressBar(getContext()))
                    .setCancelable(false)
                    .setMessage("正在上传...")
                    .create();
            Api.submitVideo(view.getContent(), new File(getPath()))
                    .before(dialog::show)
                    .after(dialog::dismiss)
                    .error((message, e) -> toast(message))
                    .success(() -> {
                        toast("发布成功");
                        finish();
                    }).run();
        });
    }

    public String getPath() {
        return getIntent().getStringExtra("path");
    }

    public static void start(Context context, File file) {
        Intent intent = new Intent(context, SubmitVideoActivity.class);
        intent.putExtra("path", file.getAbsolutePath());
        context.startActivity(intent);
    }

    public void playVideo(String path, Surface surface) {
        MediaExtractor extractor = new MediaExtractor();
        try {
            extractor.setDataSource(path);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        MediaFormat format = null;
        String mime = null;
        int trackIndex = 0;
        for (int i = 0; i < extractor.getTrackCount(); i++) {
            format = extractor.getTrackFormat(i);
            mime = format.getString(MediaFormat.KEY_MIME);
            if (mime != null && mime.startsWith("video")) {
                extractor.selectTrack(i);
                trackIndex = i;
                break;
            }
        }
        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        MediaCodec codec = null;
        try {
            codec = MediaCodec.createDecoderByType(mime);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        long duration = format == null ? 0 : format.getLong(MediaFormat.KEY_DURATION);
        int width = format == null ? 0 : format.getInteger(MediaFormat.KEY_WIDTH);
        int height = format == null ? 0 : format.getInteger(MediaFormat.KEY_HEIGHT);
        mainHandler.post(() -> view.setSurfaceSize(new Size(width, height)));
        codec.configure(format, surface, null, 0);
        codec.start();
        long time = System.currentTimeMillis();
        while (!Thread.currentThread().isInterrupted()) {
            int inputBufferIndex = codec.dequeueInputBuffer(2000);
            if (inputBufferIndex >= 0) {
                ByteBuffer buffer = codec.getInputBuffer(inputBufferIndex);
                buffer.clear();
                int sampleSize = extractor.readSampleData(buffer, 0);
                if (extractor.advance() && sampleSize > 0) {
                    codec.queueInputBuffer(inputBufferIndex, 0, sampleSize, extractor.getSampleTime(), 0);
                }
            }
            int outputBufferIndex = codec.dequeueOutputBuffer(info, 0);
            if (outputBufferIndex >= 0) {
                long diff = System.currentTimeMillis() - time;
                long sleep = info.presentationTimeUs / 1000 - diff;
                if (sleep > 0) {
                    SystemClock.sleep(sleep);
                }
                codec.releaseOutputBuffer(outputBufferIndex, true);
                if (info.presentationTimeUs >= duration - 1000000) {
                    extractor.release();
                    codec.release();
                    try {
                        extractor = new MediaExtractor();
                        extractor.setDataSource(path);
                        extractor.selectTrack(trackIndex);
                        codec = MediaCodec.createDecoderByType(mime);
                        codec.configure(format, surface, null, 0);
                        codec.start();
                        time = System.currentTimeMillis();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        extractor.release();
        codec.release();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (thread != null) {
            thread.interrupt();
        }
    }
}
