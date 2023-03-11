//
// Created by lifengqiang on 2023/3/2.
//

#include "NativeImage.h"

void NativeImage::init(int width, int height) {
    this->width = width;
    this->height = height;
    this->linesize = width * 4;
    buffer = new unsigned char[width * height * 4];
}
