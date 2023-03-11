//
// Created by lifengqiang on 2022/8/22.
//

#ifndef VIDEOUSE_NATIVEINTERFACE_H
#define VIDEOUSE_NATIVEINTERFACE_H

#include <jni.h>
#include "comm/jnimanager.h"

class NativeInterface {
protected:
    jobject mObject;
    jclass mClazz;
public:
    NativeInterface(jobject object) {
        int status;
        auto *env = android::jni::get_current_thread_env(&status);
        mClazz = env->GetObjectClass(object);
        this->mObject = env->NewGlobalRef(object);
        if (status < 0) {
            android::jni::detach_current_thread_env();
        }
    }

    ~NativeInterface() {
        int status;
        auto *env = android::jni::get_current_thread_env(&status);
        env->DeleteGlobalRef(mObject);
        if (status < 0) {
            android::jni::detach_current_thread_env();
        }
    }
};

#endif //VIDEOUSE_NATIVEINTERFACE_H
