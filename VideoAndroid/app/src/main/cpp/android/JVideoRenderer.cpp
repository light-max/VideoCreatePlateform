//
// Created by lifengqiang on 2022/12/3.
//
#include <jni.h>
#include "comm/NativeObjectManager.h"
#include "renderer/videorenderer/VideoRenderer.h"

extern "C" {
JNIEXPORT void JNICALL
Java_com_lifengqiang_video_jni_renderer_VideoRenderer_onCreated(JNIEnv *env, jobject thiz) {
    auto *renderer = android::jni::get_object<VideoRenderer>(env, thiz);
    renderer->onCreated();
}

JNIEXPORT void JNICALL
Java_com_lifengqiang_video_jni_renderer_VideoRenderer_onSizeChanged(
        JNIEnv *env, jobject thiz,
        jint width, jint height) {
    auto *renderer = android::jni::get_object<VideoRenderer>(env, thiz);
    renderer->onSizeChanged(width, height);
}

JNIEXPORT void JNICALL
Java_com_lifengqiang_video_jni_renderer_VideoRenderer_onDraw(JNIEnv *env, jobject thiz) {
    auto *renderer = android::jni::get_object<VideoRenderer>(env, thiz);
    renderer->onDraw();
}

JNIEXPORT void JNICALL
Java_com_lifengqiang_video_jni_renderer_VideoRenderer_setFrameBuffer(
        JNIEnv *env, jobject thiz, jlong handle) {
    auto *renderer = android::jni::get_object<VideoRenderer>(env, thiz);
    renderer->setNativeFrameBuffer(handle);
}

JNIEXPORT void JNICALL
Java_com_lifengqiang_video_jni_renderer_VideoRenderer_updateMatrix(
        JNIEnv *env, jobject thiz,
        jint width, jint height) {
    auto *renderer = android::jni::get_object<VideoRenderer>(env, thiz);
    renderer->updateMVPMatrix(width, height);
}

}
