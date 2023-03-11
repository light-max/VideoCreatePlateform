//
// Created by lifengqiang on 2022/11/1.
//
#include <jni.h>
#include "comm/NativeObjectManager.h"
#include "renderer/camerapreview/CameraPreviewRenderer.h"
#include "renderer/shader/shader.h"
#include "renderer/image/AndroidImage.h"
#include "renderer/image/NativeImage.h"
#include "media/encoder/FFMediaEncoder.h"

extern "C" {
JNIEXPORT jlong JNICALL
Java_com_lifengqiang_video_jni_interfaces_NativeObject_createNativeObject(
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
Java_com_lifengqiang_video_jni_interfaces_NativeObject_destroyNativeObject(
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
Java_com_lifengqiang_video_jni_NativeFunctionSetup_registerNativeObject(
        JNIEnv *env, jclass clazz) {
    auto *manager = android::NativeObjectManager::get_instance();
    manager->register_class<CameraPreviewRenderer>(
            "com.lifengqiang.video.jni.renderer.CameraPreviewRenderer");
    manager->register_class<AndroidImage>(
            "com.lifengqiang.video.jni.renderer.AndroidImage");
    manager->register_class<NativeImage>(
            "com.lifengqiang.video.jni.renderer.NativeImage");
    manager->register_class<FFMediaEncoder>(
            "com.lifengqiang.video.jni.encoder.FFMediaEncoder");
}

}

extern "C"
JNIEXPORT void JNICALL
Java_com_lifengqiang_video_jni_NativeFunctionSetup_registerAssetManager(
        JNIEnv *env, jclass clazz,
        jobject manager) {
    shader::registerAssertManager(env, manager);
}