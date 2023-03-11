package com.lifengqiang.video.jni.renderer;

import android.opengl.GLSurfaceView;

import com.lifengqiang.video.camera.CameraPreviewView;
import com.lifengqiang.video.jni.interfaces.NativeObject;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CameraPreviewRenderer extends NativeObject implements GLSurfaceView.Renderer {
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

    public native void setImageCanvas(int width, int height, int angle);

    public native void setMirror(int mirror);

    public native void drawImage(AndroidImage image);

    public native void setCallback(CameraPreviewView.OnFrameRenderCallback callback);

    public native NativeImage getRendererFrameBuffer();

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        super.releaseNativeObject();
    }
}
