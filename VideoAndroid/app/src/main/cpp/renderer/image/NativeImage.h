//
// Created by lifengqiang on 2023/3/2.
//

#ifndef VIDEOSHARE_NATIVEIMAGE_H
#define VIDEOSHARE_NATIVEIMAGE_H

#include "Mutex.h"

class NativeImage : public Mutex {
public:
    int width, height, linesize;
    unsigned char *buffer = nullptr;

    void init(int width, int height);
};

#endif //VIDEOSHARE_NATIVEIMAGE_H
