//
// Created by lifengqiang on 2022/11/1.
//
#include <jni.h>
#include "comm/NativeObjectManager.h"

extern "C" {
JNIEXPORT jlong JNICALL
Java_com_lifengqiang_filtercamera_interfaces_NativeObject_createNativeObject(
        JNIEnv *env,
        jobject thiz,
        jstring class_name) {
    auto *manager = android::NativeObjectManager::get_instance();
    const char *className = env->GetStringUTFChars(class_name, nullptr);
    auto *factory = manager->get_factory(className);
    auto *wrapper = factory->create_wrapper();
    return (jlong) wrapper;
}

JNIEXPORT void JNICALL
Java_com_lifengqiang_filtercamera_interfaces_NativeObject_destroyNativeObject(
        JNIEnv *env,
        jobject thiz,
        jstring class_name,
        jlong handler) {
    if (handler != 0) {
        const char *cn = env->GetStringUTFChars(class_name, nullptr);
        auto *manager = android::NativeObjectManager::get_instance();
        android::BaseNativeObjectWrapperFactory *factory = manager->get_factory(cn);
        factory->release_wrapper(handler);
    }
}

JNIEXPORT void JNICALL
Java_com_lifengqiang_filtercamera_renderer_NativeFunctionSetup_registerNativeObject(
        JNIEnv *env, jclass clazz) {
    auto *manager = android::NativeObjectManager::get_instance();
}

}