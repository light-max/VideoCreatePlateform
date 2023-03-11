package com.lifengqiang.video.camera;

import android.content.Context;
import android.media.Image;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Size;

import com.lifengqiang.video.jni.renderer.AndroidImage;
import com.lifengqiang.video.jni.renderer.CameraPreviewRenderer;
import com.lifengqiang.video.jni.renderer.NativeImage;

public class CameraPreviewView extends GLSurfaceView {
    private CameraPreviewRenderer renderer;
    private Size size;
    private int angle;

    private OnFrameRenderCallback callback;

    public CameraPreviewView(Context context) {
        this(context, null);
    }

    public CameraPreviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(3);
        renderer = new CameraPreviewRenderer();
        setRenderer(renderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public void setCallback(OnFrameRenderCallback callback) {
        this.callback = callback;
        if (renderer != null) {
            renderer.setCallback(callback);
        }
    }

    public void renderImage(Image image) {
        if (renderer != null) {
            renderer.drawImage(new AndroidImage(image));
            requestRender();
        }
    }

    public void setCanvas(Size size, int angle) {
        if (renderer != null) {
            renderer.setImageCanvas(size.getWidth(), size.getHeight(), angle);
            this.size = size;
            this.angle = angle;
        }
    }

    public void setMirror(int mirror) {
        if (renderer != null) {
            renderer.setMirror(mirror);
        }
    }

    public CameraPreviewRenderer getRenderer() {
        return renderer;
    }

    public interface OnFrameRenderCallback {
        void onRendererFrame(NativeImage image);
    }
}
