//
// Created by lifengqiang on 2022/8/22.
//

#include "NativeObjectManager.h"

namespace android {

    NativeObjectManager *NativeObjectManager::instance;

    NativeObjectManager *NativeObjectManager::get_instance() {
        if (instance == nullptr) {
            instance = new NativeObjectManager();
        }
        return instance;
    }
}
