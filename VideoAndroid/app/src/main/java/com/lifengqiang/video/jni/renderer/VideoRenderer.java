package com.lifengqiang.video.jni.renderer;

import android.opengl.GLSurfaceView;

import com.lifengqiang.video.jni.interfaces.NativeObject;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class VideoRenderer extends NativeObject implements GLSurfaceView.Renderer {
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        onCreated();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        onSizeChanged(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        onDraw();
    }

    public native void onCreated();

    public native void onSizeChanged(int width, int height);

    public native void onDraw();

    public native void setFrameBuffer(long handle);

    public native void updateMatrix(int width, int height);

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        super.releaseNativeObject();
    }

    public void release() {
        super.releaseNativeObject();
    }
}
