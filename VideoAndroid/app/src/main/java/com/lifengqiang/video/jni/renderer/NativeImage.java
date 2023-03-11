package com.lifengqiang.video.jni.renderer;

import com.lifengqiang.video.jni.interfaces.NativeObject;

import java.nio.ByteBuffer;

/**
 * OpenGLES离屏渲染后产生的图片
 * 格式为RGBA_8888
 * 图片尺寸默认为竖屏1080*1920
 * 在JNI中使用和释放
 */
public class NativeImage extends NativeObject {
    private NativeImage() {
    }

    public native int getWidth();

    public native int getHeight();

    public native ByteBuffer getBuffer();
}
