package com.lifengqiang.video.jni;

import android.content.Context;
import android.content.res.AssetManager;

public class NativeFunctionSetup {
    private static boolean hasSetup = false;

    public static void setup(Context context) {
        synchronized (NativeFunctionSetup.class) {
            if (!hasSetup) {
                hasSetup = true;
                System.loadLibrary("renderer");
                registerNativeObject();
                registerAssetManager(context.getAssets());
            }
        }
    }

    public static native void registerNativeObject();

    public static native void registerAssetManager(AssetManager manager);
}
