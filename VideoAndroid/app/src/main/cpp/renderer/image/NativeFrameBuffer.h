//
// Created by lifengqiang on 2023/3/16.
//

#ifndef VIDEOSHARE_NATIVEFRAMEBUFFER_H
#define VIDEOSHARE_NATIVEFRAMEBUFFER_H

#include "comm/Mutex.h"

class NativeFrameBuffer : public Mutex {
public:
    int width = 0, height = 0;
    unsigned char *data = nullptr;
//    unsigned char *y = nullptr;
//    unsigned char *u = nullptr;
//    unsigned char *v = nullptr;

public:
    void createData() {
        if (data != nullptr) delete data;
        data = new unsigned char[width * height * 4];
//        if (y != nullptr) delete y;
//        if (u != nullptr) delete u;
//        if (v != nullptr) delete v;
//        y = new unsigned char[width * height];
//        u = new unsigned char[width * height / 4];
//        v = new unsigned char[width * height / 4];
    }

    void clearData() {
        if (data != nullptr) delete data;
        data = nullptr;
        width = height = 0;
    }

    bool hasData() {
        return width != 0 && height != 0;
    }
//
//    unsigned char *getY() {
////        return data;
//        return y;
//    }
//
//    unsigned char *getU() {
////        return getY() + width * height;
//        return u;
//    }
//
//    unsigned char *getV() {
////        return getU() + width * height / 4;
//        return v;
//    }
};


#endif //VIDEOSHARE_NATIVEFRAMEBUFFER_H
