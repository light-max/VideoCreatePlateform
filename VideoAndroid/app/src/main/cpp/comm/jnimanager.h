//
// Created by lifengqiang on 2022/8/22.
//

#ifndef VIDEOUSE_JNIMANAGER_H
#define VIDEOUSE_JNIMANAGER_H

#include <jni.h>

extern "C" {

namespace android {
    namespace jni {
        JavaVM *getjvm();

        JNIEnv *get_current_thread_env(int *status);

        void detach_current_thread_env();
    }
}

}

#endif //VIDEOUSE_JNIMANAGER_H
