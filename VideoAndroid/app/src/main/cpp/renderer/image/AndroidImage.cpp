//
// Created by lifengqiang on 2023/1/30.
//

#include "AndroidImage.h"

void AndroidImage::setImageSize(int width, int height) {
    this->width = width;
    this->height = height;
}

void AndroidImage::setPlanesBuffer(unsigned char *y, unsigned char *u, unsigned char *v) {
    this->planes[0] = y;
    this->planes[1] = u;
    this->planes[2] = v;
}

void AndroidImage::setPlanesPixelStride(int y, int u, int v) {
    this->planes_pixel_stride[0] = y;
    this->planes_pixel_stride[1] = u;
    this->planes_pixel_stride[2] = v;
}

void AndroidImage::setPlanesRowStride(int y, int u, int v) {
    this->planes_row_stride[0] = y;
    this->planes_row_stride[1] = u;
    this->planes_row_stride[2] = v;
}

void AndroidImage::setTimestampNS(int timestamp_ns) {
    this->timestamp_ns = timestamp_ns;
}