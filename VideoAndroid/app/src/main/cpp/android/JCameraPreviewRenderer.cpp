//
// Created by lifengqiang on 2023/1/30.
//

#include "comm/NativeObjectManager.h"
#include "renderer/camerapreview/CameraPreviewRenderer.h"

extern "C" {
JNIEXPORT void JNICALL
Java_com_lifengqiang_video_jni_renderer_CameraPreviewRenderer_onCreated(
        JNIEnv *env, jobject thiz) {
    auto *renderer = android::jni::get_object<CameraPreviewRenderer>(env, thiz);
    renderer->onSurfaceCreated();
}

JNIEXPORT void JNICALL
Java_com_lifengqiang_video_jni_renderer_CameraPreviewRenderer_onSizeChanged(
        JNIEnv *env, jobject thiz,
        jint width, jint height) {
    auto *renderer = android::jni::get_object<CameraPreviewRenderer>(env, thiz);
    renderer->onSurfaceChanged(width, height);
}

JNIEXPORT void JNICALL
Java_com_lifengqiang_video_jni_renderer_CameraPreviewRenderer_onDraw(
        JNIEnv *env, jobject thiz) {
    auto *renderer = android::jni::get_object<CameraPreviewRenderer>(env, thiz);
    renderer->onDraw();
}

JNIEXPORT void JNICALL
Java_com_lifengqiang_video_jni_renderer_CameraPreviewRenderer_drawImage(
        JNIEnv *env, jobject thiz,
        jobject image) {
    auto *renderer = android::jni::get_object<CameraPreviewRenderer>(env, thiz);
    auto *jimage = android::jni::get_object<AndroidImage>(env, image);
    renderer->gli420Frame.copyDataByAndroidImage(jimage);
}

JNIEXPORT void JNICALL
Java_com_lifengqiang_video_jni_renderer_CameraPreviewRenderer_setImageCanvas(
        JNIEnv *env, jobject thiz,
        jint width, jint height, jint angle) {
    auto *renderer = android::jni::get_object<CameraPreviewRenderer>(env, thiz);
    renderer->setImageCanvas(width, height, angle);
}

JNIEXPORT void JNICALL
Java_com_lifengqiang_video_jni_renderer_CameraPreviewRenderer_setMirror(
        JNIEnv *env, jobject thiz, jint mirror) {
    auto *renderer = android::jni::get_object<CameraPreviewRenderer>(env, thiz);
    renderer->gli420Frame.mirror = mirror;
}

JNIEXPORT void JNICALL
Java_com_lifengqiang_video_jni_renderer_CameraPreviewRenderer_setCallback(
        JNIEnv *env, jobject thiz,
        jobject callback) {
    auto *renderer = android::jni::get_object<CameraPreviewRenderer>(env, thiz);
    auto *caller = new OnFrameRendererCallback(callback);
    renderer->setFrameRendererCallback(caller);
}

JNIEXPORT jobject JNICALL
Java_com_lifengqiang_video_jni_renderer_CameraPreviewRenderer_getRendererFrameBuffer(
        JNIEnv *env,
        jobject thiz) {
    auto *renderer = android::jni::get_object<CameraPreviewRenderer>(env, thiz);
    return renderer->callback->getJImage();
}

}

