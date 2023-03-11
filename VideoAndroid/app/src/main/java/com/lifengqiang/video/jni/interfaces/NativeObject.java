package com.lifengqiang.video.jni.interfaces;

public class NativeObject {
    private long mHandler;

    public NativeObject() {
        mHandler = createNativeObject(this.getClass().getName());
    }

    public void releaseNativeObject() {
        destroyNativeObject(this.getClass().getName(), mHandler);
        mHandler = 0;
    }

    public native long createNativeObject(String className);

    public native void destroyNativeObject(String className, long handler);

    public long getNativeHandler() {
        return mHandler;
    }
}
