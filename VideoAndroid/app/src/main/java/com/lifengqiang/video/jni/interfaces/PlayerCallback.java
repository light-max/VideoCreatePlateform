package com.lifengqiang.video.jni.interfaces;

public interface PlayerCallback extends NativeInterface {
    void onVideoPrepare(String path, int duration, int width, int height);

    void onGoon();

    void onPause();

    void onProgress(int duration, int progress);

    void onRelease();

    void onError(String message);

    void onFinish();
}
