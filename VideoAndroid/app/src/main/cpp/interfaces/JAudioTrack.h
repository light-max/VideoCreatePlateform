//
// Created by lifengqiang on 2023/3/19.
//

#ifndef VIDEOSHARE_JAUDIOTRACK_H
#define VIDEOSHARE_JAUDIOTRACK_H

#include "NativeInterface.h"

class JAudioTrack : public NativeInterface {
private:
    jmethodID methodId;
public:
    JAudioTrack(jobject object) : NativeInterface(object) {
        int status;
        JNIEnv *env = android::jni::get_current_thread_env(&status);
        methodId = env->GetMethodID(mClazz, "write", "(Ljava/nio/ByteBuffer;II)I");
        if (status < 0) {
            android::jni::detach_current_thread_env();
        }
    }

    void write(unsigned char *data, size_t size) {
        int status;
        JNIEnv *env = android::jni::get_current_thread_env(&status);
        jobject buffer = env->NewDirectByteBuffer(data, size);
        env->CallIntMethod(mObject, methodId, buffer, size, 0);
        if (status < 0) {
            android::jni::detach_current_thread_env();
        }
    }
};

#endif //VIDEOSHARE_JAUDIOTRACK_H
