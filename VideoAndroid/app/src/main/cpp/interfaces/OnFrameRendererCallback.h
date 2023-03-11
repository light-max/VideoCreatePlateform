//
// Created by lifengqiang on 2023/3/1.
//

#ifndef VIDEOSHARE_ONFRAMERENDERERCALLBACK_H
#define VIDEOSHARE_ONFRAMERENDERERCALLBACK_H

#include "NativeInterface.h"
#include "renderer/image/NativeImage.h"
#include "comm/NativeObjectManager.h"

class OnFrameRendererCallback : NativeInterface, Mutex {
private:
    jmethodID mFrame;
    jclass mClassNativeImage;
    jmethodID mNativeImageInit;
    jobject mJImage;
    NativeImage *mImage;
public:
    OnFrameRendererCallback(jobject object) : NativeInterface(object) {
        int status;
        auto *env = android::jni::get_current_thread_env(&status);
        mFrame = env->GetMethodID(mClazz, "onRendererFrame",
                                  "(Lcom/lifengqiang/video/jni/renderer/NativeImage;)V");
        mClassNativeImage = env->FindClass(
                "com/lifengqiang/video/jni/renderer/NativeImage");
        mNativeImageInit = env->GetMethodID(mClassNativeImage, "<init>", "()V");
        mJImage = env->NewObject(mClassNativeImage, mNativeImageInit);
        mJImage = env->NewGlobalRef(mJImage);
        mImage = android::jni::get_object<NativeImage>(env, mJImage);
        mImage->init(1080, 1920);
        if (status < 0) {
            android::jni::detach_current_thread_env();
        }
    }

    ~OnFrameRendererCallback() {
        int status;
        auto *env = android::jni::get_current_thread_env(&status);
        env->DeleteGlobalRef(mJImage);
        if (status < 0) {
            android::jni::detach_current_thread_env();
        }
        delete mImage;
    }

    void draw(unsigned char *data, int size) {
        mImage->lock();
        memcpy(mImage->buffer, data, size);
        mImage->unlock();
        int status;
        auto *env = android::jni::get_current_thread_env(&status);
        env->CallVoidMethod(mObject, mFrame, mJImage);
        if (status < 0) {
            android::jni::detach_current_thread_env();
        }
    }

    jobject getJImage() {
        return mJImage;
    }
};

#endif //VIDEOSHARE_ONFRAMERENDERERCALLBACK_H
