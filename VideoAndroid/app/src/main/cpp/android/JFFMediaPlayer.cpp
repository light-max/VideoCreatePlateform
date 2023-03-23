//
// Created by lifengqiang on 2022/12/3.
//
#include <jni.h>
#include "comm/NativeObjectManager.h"
#include "media/player/FFMediaPlayer.h"

extern "C" {

JNIEXPORT JNICALL void
Java_com_lifengqiang_video_jni_player_FFMediaPlayer_setPath(
        JNIEnv *env, jobject thiz, jstring path) {
    auto *player = android::jni::get_object<FFMediaPlayer>(env, thiz);
    player->setPath(env->GetStringUTFChars(path, nullptr));
}

JNIEXPORT JNICALL void
Java_com_lifengqiang_video_jni_player_FFMediaPlayer_goon(
        JNIEnv *env, jobject thiz) {
    auto *player = android::jni::get_object<FFMediaPlayer>(env, thiz);
    player->goon();
}

JNIEXPORT JNICALL void
Java_com_lifengqiang_video_jni_player_FFMediaPlayer_pause(
        JNIEnv *env, jobject thiz) {
    auto *player = android::jni::get_object<FFMediaPlayer>(env, thiz);
    player->pause();
}

JNIEXPORT void JNICALL
Java_com_lifengqiang_video_jni_player_FFMediaPlayer_toggle(
        JNIEnv *env, jobject thiz) {
    auto *player = android::jni::get_object<FFMediaPlayer>(env, thiz);
    player->toggle();
}

JNIEXPORT JNICALL void
Java_com_lifengqiang_video_jni_player_FFMediaPlayer_seekTo(
        JNIEnv *env, jobject thiz, jint progress) {
    auto *player = android::jni::get_object<FFMediaPlayer>(env, thiz);
    player->seekTo(progress);
}

JNIEXPORT JNICALL void
Java_com_lifengqiang_video_jni_player_FFMediaPlayer_resume(
        JNIEnv *env, jobject thiz) {
    auto *player = android::jni::get_object<FFMediaPlayer>(env, thiz);
    player->resume();
}

JNIEXPORT void JNICALL
Java_com_lifengqiang_video_jni_player_FFMediaPlayer_registerCallback(
        JNIEnv *env, jobject thiz, jobject callback) {
    auto *player = android::jni::get_object<FFMediaPlayer>(env, thiz);
    OnVideoPlayerCallback *call = nullptr;
    if (callback != nullptr) {
        call = new OnVideoPlayerCallback(callback);
    }
    player->registerPlayerCallback(call);
}

JNIEXPORT jlong JNICALL
Java_com_lifengqiang_video_jni_player_FFMediaPlayer_getFrameBuffer(
        JNIEnv *env, jobject thiz) {
    auto *player = android::jni::get_object<FFMediaPlayer>(env, thiz);
    return (long) (player->getFrameBuffer());
}

JNIEXPORT void JNICALL
Java_com_lifengqiang_video_jni_player_FFMediaPlayer_setGLSurfaceView(
        JNIEnv *env, jobject thiz,
        jobject surface_view) {
    auto *player = android::jni::get_object<FFMediaPlayer>(env, thiz);
    auto *view = new JGLSurfaceView(surface_view);
    player->setGLSurfaceView(view);
}

JNIEXPORT void JNICALL
Java_com_lifengqiang_video_jni_player_FFMediaPlayer_waitPrepare(
        JNIEnv *env, jobject thiz) {
    auto *player = android::jni::get_object<FFMediaPlayer>(env, thiz);
    player->waitPrepare();
}

JNIEXPORT void JNICALL
Java_com_lifengqiang_video_jni_player_FFMediaPlayer_clearFrameBuffer(
        JNIEnv *env, jobject thiz) {
    auto *player = android::jni::get_object<FFMediaPlayer>(env, thiz);
    player->clearFrameBuffer();
}

JNIEXPORT void JNICALL
Java_com_lifengqiang_video_jni_player_FFMediaPlayer_setAudioTrack(
        JNIEnv *env, jobject thiz,
        jobject track) {
    auto *player = android::jni::get_object<FFMediaPlayer>(env, thiz);
    auto *audio_track = new JAudioTrack(track);
    player->setAudioTrack(audio_track);
}

}

