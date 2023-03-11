package com.lifengqiang.video.jni.encoder;

import com.lifengqiang.video.jni.interfaces.NativeObject;
import com.lifengqiang.video.jni.renderer.NativeImage;

public class FFMediaEncoder extends NativeObject {
    public native void startEncode(String path);

    public native void stopEncode();

    public native void onAudioData(byte[] data, int size);

    public native void onVideoData(NativeImage image);
}
