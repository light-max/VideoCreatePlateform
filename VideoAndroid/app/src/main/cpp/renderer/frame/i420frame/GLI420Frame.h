//
// Created by lifengqiang on 2023/1/30.
//

#ifndef VIDEOSHARE_GLI420FRAME_H
#define VIDEOSHARE_GLI420FRAME_H

#include "renderer/image/AndroidImage.h"
#include <malloc.h>
#include <string.h>
#include "Mutex.h"
#include "comm/mylog.h"

class GLI420Frame : public Mutex {
public:
    int width = 0, height = 0, angle = 0, mirror = 0;
    unsigned char *data = nullptr;
    unsigned char *plane_y = nullptr;
    unsigned char *plane_u = nullptr;
    unsigned char *plane_v = nullptr;
public:
    ~GLI420Frame() {
        if (data != nullptr) {
            free(data);
        }
    }

    void setImageCanvas(int width, int height, int angle);

    void copyDataByAndroidImage(AndroidImage *image);
};


#endif //VIDEOSHARE_GLI420FRAME_H
