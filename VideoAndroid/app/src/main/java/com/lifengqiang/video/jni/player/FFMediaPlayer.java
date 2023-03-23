package com.lifengqiang.video.jni.player;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioTrack;
import android.opengl.GLSurfaceView;

import com.lifengqiang.video.jni.interfaces.NativeObject;
import com.lifengqiang.video.jni.interfaces.PlayerCallback;

public class FFMediaPlayer extends NativeObject {
    private AudioTrack track;

    public FFMediaPlayer() {
        initAudioTrack();
    }

    public native void setPath(String path);

    public native void goon();

    public native void pause();

    public native void toggle();

    public native void resume();

    public native void registerCallback(PlayerCallback callback);

    public native void seekTo(int second);

    public native long getFrameBuffer();

    public native void setGLSurfaceView(GLSurfaceView surfaceView);

    public native void waitPrepare();

    public native void clearFrameBuffer();

    public void initAudioTrack() {
        if (track != null) {
            track.stop();
            track.release();
        }
        int minBufferSize = AudioTrack.getMinBufferSize(44100, 2, AudioFormat.ENCODING_PCM_16BIT);
        track = new AudioTrack.Builder()
                .setAudioFormat(new AudioFormat.Builder()
                        .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)
                        .setSampleRate(44100)
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .build())
                .setAudioAttributes(new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
                        .build()
                )
                .setTransferMode(AudioTrack.MODE_STREAM)
                .setBufferSizeInBytes(minBufferSize)
                .build();
        track.play();
        setAudioTrack(track);
    }

    public native void setAudioTrack(AudioTrack track);

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        super.releaseNativeObject();
        if (track != null) {
            track.stop();
            track.release();
        }
    }
}
