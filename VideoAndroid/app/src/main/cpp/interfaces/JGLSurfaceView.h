//
// Created by lifengqiang on 2023/3/18.
//

#ifndef VIDEOSHARE_JGLSURFACEVIEW_H
#define VIDEOSHARE_JGLSURFACEVIEW_H

#include "NativeInterface.h"

class JGLSurfaceView : NativeInterface {
private:
    jmethodID requestRender;
public:
    JGLSurfaceView(jobject object) : NativeInterface(object) {
        int status;
        JNIEnv *env = android::jni::get_current_thread_env(&status);
        requestRender = env->GetMethodID(mClazz, "requestRender", "()V");
        if (status < 0) {
            android::jni::detach_current_thread_env();
        }
    }

    void render() {
        int status;
        JNIEnv *env = android::jni::get_current_thread_env(&status);
        env->CallVoidMethod(mObject, requestRender);
        if (status < 0) {
            android::jni::detach_current_thread_env();
        }
    }
};

#endif //VIDEOSHARE_JGLSURFACEVIEW_H
