package com.lifengqiang.video.ui.main.home.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lifengqiang.video.R;
import com.lifengqiang.video.data.result.Works;
import com.lifengqiang.video.jni.interfaces.PlayerCallback;
import com.lifengqiang.video.jni.player.FFMediaPlayer;
import com.lifengqiang.video.jni.renderer.VideoRenderer;
import com.lifengqiang.video.ui.main.home.loader.HttpVideoLoader;

import java.io.File;

public class VideoPlayerView extends LinearLayout implements HttpVideoLoader.Call, PlayerCallback {
    private VideoViewContainer videoViewContainer;
    private GLSurfaceView surfaceView;
    private ImageView toggle;
    private FFMediaPlayer player = new FFMediaPlayer();
    private VideoRenderer renderer = new VideoRenderer();
    private VideoSeekBar seek;

    private Works works;

    private Handler mainHandler = new Handler(Looper.getMainLooper());

    public VideoPlayerView(Context context) {
        this(context, null);
    }

    public VideoPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context, R.layout.view_video_surface, this);
        player.registerCallback(this);
        renderer.setFrameBuffer(player.getFrameBuffer());
        surfaceView = findViewById(R.id.surface);
        toggle = findViewById(R.id.toggle);
        surfaceView.setEGLContextClientVersion(3);
        surfaceView.setRenderer(renderer);
        player.setGLSurfaceView(surfaceView);
        surfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        toggle.setOnClickListener(v -> player.toggle());
        seek = findViewById(R.id.seek);
    }

    public void onResume() {
        surfaceView.onResume();
        player.goon();
    }

    public void onLifePause() {
        surfaceView.onPause();
        player.pause();
    }

    public void setWorks(Works works) {
        this.works = works;
        player.clearFrameBuffer();
        HttpVideoLoader.load(getContext(), works.getVideo(), this);
    }

    public void setVideoViewContainer(VideoViewContainer videoViewContainer) {
        this.videoViewContainer = videoViewContainer;
    }

    public void pause() {
        player.pause();
    }

    @Override
    public void onSuccess(File file) {
        if (file.equals(HttpVideoLoader.getFileByUrl(getContext(), works.getVideo()))) {
            player.setPath(file.getAbsolutePath());
            player.goon();
        }
    }

    @Override
    public void onVideoPrepare(String path, int duration, int width, int height) {
        renderer.updateMatrix(width, height);
        player.waitPrepare();
        mainHandler.post(() -> {
            seek.setProgress(0);
            seek.setMax(duration);
            videoViewContainer.hideCover();
            setVisibility(VISIBLE);
        });
    }

    @Override
    public void onGoon() {
        if (toggle == null) return;
        mainHandler.post(() -> toggle.setImageResource(0));
    }

    @Override
    public void onPause() {
        if (toggle == null) return;
        mainHandler.post(() -> toggle.setImageResource(R.drawable.ic_baseline_play_arrow_24));
    }

    @Override
    public void onProgress(int duration, int progress) {
        mainHandler.post(() -> {
            seek.setProgress(progress);
        });
    }

    @Override
    public void onRelease() {
    }

    @Override
    public void onError(String msg) {
        mainHandler.post(() -> {
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onFinish() {
//        mainHandler.postDelayed(() -> {
//            player.seekTo(0);
//            player.goon();
//        },500);
    }
}
