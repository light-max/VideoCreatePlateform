//
// Created by lifengqiang on 2023/1/30.
//
#include "comm/NativeObjectManager.h"
#include "media/encoder//FFMediaEncoder.h"

extern "C" {

JNIEXPORT void JNICALL
Java_com_lifengqiang_video_jni_encoder_FFMediaEncoder_startEncode(
        JNIEnv *env, jobject thiz, jstring path) {
    auto *encoder = android::jni::get_object<FFMediaEncoder>(env, thiz);
    encoder->startEncode(env->GetStringUTFChars(path, nullptr));
}

JNIEXPORT void JNICALL
Java_com_lifengqiang_video_jni_encoder_FFMediaEncoder_stopEncode(
        JNIEnv *env, jobject thiz) {
    auto *encoder = android::jni::get_object<FFMediaEncoder>(env, thiz);
    encoder->stopEncode();
}

}
extern "C"
JNIEXPORT void JNICALL
Java_com_lifengqiang_video_jni_encoder_FFMediaEncoder_onAudioData(
        JNIEnv *env, jobject thiz,
        jbyteArray bytes, jint size) {
    auto *encoder = android::jni::get_object<FFMediaEncoder>(env, thiz);
    auto *data = (unsigned char *) malloc(size);
    env->GetByteArrayRegion(bytes, 0, size, (signed char *) data);
    encoder->OnFrameEncode(new AudioFrame(data, size, false));
}

extern "C"
JNIEXPORT void JNICALL
Java_com_lifengqiang_video_jni_encoder_FFMediaEncoder_onVideoData(
        JNIEnv *env, jobject thiz,
        jobject jimage) {
    auto *encoder = android::jni::get_object<FFMediaEncoder>(env, thiz);
    auto *image = android::jni::get_object<NativeImage>(env, jimage);
    encoder->OnFrameEncode(image);
}
