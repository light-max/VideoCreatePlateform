//
// Created by lifengqiang on 2022/12/3.
//
#include <jni.h>
#include "comm/NativeObjectManager.h"
#include "renderer/image/AndroidImage.h"
#include "renderer/image/NativeImage.h"

extern "C" {

JNIEXPORT void JNICALL
Java_com_lifengqiang_video_jni_renderer_AndroidImage_setImageSize(
        JNIEnv *env, jobject thiz,
        jint width, jint height) {
    auto *object = android::jni::get_object<AndroidImage>(env, thiz);
    object->setImageSize(width, height);
}

JNIEXPORT void JNICALL
Java_com_lifengqiang_video_jni_renderer_AndroidImage_setPlanesBuffer(
        JNIEnv *env, jobject thiz,
        jobject y,
        jobject u,
        jobject v) {
    unsigned char *planes[3] = {
            (unsigned char *) env->GetDirectBufferAddress(y),
            (unsigned char *) env->GetDirectBufferAddress(u),
            (unsigned char *) env->GetDirectBufferAddress(v),
    };
    auto *object = android::jni::get_object<AndroidImage>(env, thiz);
    object->setPlanesBuffer(planes[0], planes[1], planes[2]);
}

JNIEXPORT void JNICALL
Java_com_lifengqiang_video_jni_renderer_AndroidImage_setPlanesPixelStride(
        JNIEnv *env, jobject thiz,
        jint y, jint u, jint v) {
    auto *object = android::jni::get_object<AndroidImage>(env, thiz);
    object->setPlanesPixelStride(y, u, v);
}

JNIEXPORT void JNICALL
Java_com_lifengqiang_video_jni_renderer_AndroidImage_setPlanesRowStride(
        JNIEnv *env, jobject thiz,
        jint y, jint u, jint v) {
    auto *object = android::jni::get_object<AndroidImage>(env, thiz);
    object->setPlanesRowStride(y, u, v);
}

JNIEXPORT void JNICALL
Java_com_lifengqiang_video_jni_renderer_AndroidImage_setTimestampNS(
        JNIEnv *env, jobject thiz,
        jlong timestamp_ns) {
    auto *object = android::jni::get_object<AndroidImage>(env, thiz);
    object->setTimestampNS(timestamp_ns);
}

JNIEXPORT jobject JNICALL
Java_com_lifengqiang_video_jni_renderer_NativeImage_getBuffer(
        JNIEnv *env, jobject thiz) {
    auto *image = android::jni::get_object<NativeImage>(env, thiz);
    auto *buffer = env->NewDirectByteBuffer(image->buffer, image->linesize * image->height);
    return buffer;
}

JNIEXPORT jint JNICALL
Java_com_lifengqiang_video_jni_renderer_NativeImage_getWidth(
        JNIEnv *env, jobject thiz) {
    auto *image = android::jni::get_object<NativeImage>(env, thiz);
    return image->width;
}

JNIEXPORT jint JNICALL
Java_com_lifengqiang_video_jni_renderer_NativeImage_getHeight(
        JNIEnv *env, jobject thiz) {
    auto *image = android::jni::get_object<NativeImage>(env, thiz);
    return image->height;
}

}

