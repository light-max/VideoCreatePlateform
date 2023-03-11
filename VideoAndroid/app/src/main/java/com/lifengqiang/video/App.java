package com.lifengqiang.video;

import android.app.Application;

import com.lifengqiang.video.jni.NativeFunctionSetup;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        NativeFunctionSetup.setup(getApplicationContext());
    }
}
