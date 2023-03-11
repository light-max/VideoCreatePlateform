//
// Created by lifengqiang on 2022/9/5.
//

#ifndef VIDEOUSE_ONVIDEOPLAYERCALLBACK_H
#define VIDEOUSE_ONVIDEOPLAYERCALLBACK_H

#include "NativeInterface.h"

class OnVideoPlayerCallback : public NativeInterface {
private:
    jmethodID mPrepare;
    jmethodID mGoon;
    jmethodID mPause;
    jmethodID mProgress;
    jmethodID mRelease;
    jmethodID mError;
    jmethodID mFinish;
public:
    OnVideoPlayerCallback(jobject object) : NativeInterface(object) {
        int status;
        auto *env = android::jni::get_current_thread_env(&status);
        mPrepare = env->GetMethodID(mClazz, "onVideoPrepare", "(Ljava/lang/String;III)V");
        mGoon = env->GetMethodID(mClazz, "onGoon", "()V");
        mPause = env->GetMethodID(mClazz, "onPause", "()V");
        mProgress = env->GetMethodID(mClazz, "onProgress", "(II)V");
        mRelease = env->GetMethodID(mClazz, "onRelease", "()V");
        mError = env->GetMethodID(mClazz, "onError", "(Ljava/lang/String;)V");
        mFinish = env->GetMethodID(mClazz, "onFinish", "()V");
        if (status < 0) {
            android::jni::detach_current_thread_env();
        }
    }

    void prepare(const char *path, int duration, int width, int height) {
        int status;
        auto *env = android::jni::get_current_thread_env(&status);
        jstring s = env->NewStringUTF(path);
        env->CallVoidMethod(mObject, mPrepare, s, duration, width, height);
        if (status < 0) {
            android::jni::detach_current_thread_env();
        }
    }

    void goon() {
        int status;
        auto *env = android::jni::get_current_thread_env(&status);
        env->CallVoidMethod(mObject, mGoon);
        if (status < 0) {
            android::jni::detach_current_thread_env();
        }
    }

    void pause() {
        int status;
        auto *env = android::jni::get_current_thread_env(&status);
        env->CallVoidMethod(mObject, mPause);
        if (status < 0) {
            android::jni::detach_current_thread_env();
        }
    }

    void progress(int duration, int progress) {
        int status;
        auto *env = android::jni::get_current_thread_env(&status);
        env->CallVoidMethod(mObject, mProgress, duration, progress);
        if (status < 0) {
            android::jni::detach_current_thread_env();
        }
    }

    void release() {
        int status;
        auto *env = android::jni::get_current_thread_env(&status);
        env->CallVoidMethod(mObject, mRelease);
        if (status < 0) {
            android::jni::detach_current_thread_env();
        }
    }

    void error(const char *message) {
        int status;
        auto *env = android::jni::get_current_thread_env(&status);
        jstring s = env->NewStringUTF(message);
        env->CallVoidMethod(mObject, mError, s);
        if (status < 0) {
            android::jni::detach_current_thread_env();
        }
    }

    void finish() {
        int status;
        auto *env = android::jni::get_current_thread_env(&status);
        env->CallVoidMethod(mObject, mFinish);
        if (status < 0) {
            android::jni::detach_current_thread_env();
        }
    }
};

#endif //VIDEOUSE_ONVIDEOPLAYERCALLBACK_H
