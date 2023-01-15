//
// Created by lifengqiang on 2022/8/22.
//

#include <jni.h>
#include "jnimanager.h"
#include "mylog.h"

extern "C" {

JavaVM *jvm = nullptr;

JNIEXPORT jint
JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env;
    jvm = vm;
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        return -1;
    }
    return JNI_VERSION_1_6;
}

JavaVM *android::jni::getjvm() {
    return jvm;
}

JNIEnv *android::jni::get_current_thread_env(int *status) {
    if (status == nullptr) {
        status = new int;
    }
    JNIEnv *env = nullptr;
    int result = jvm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6);
    *status = result;
    if (result < 0) {
        result = jvm->AttachCurrentThread(&env, nullptr);
        if (result < 0) {
            LOGI("empty");
        }
    }
    return env;
}

void android::jni::detach_current_thread_env() {
    jvm->DetachCurrentThread();
}

}