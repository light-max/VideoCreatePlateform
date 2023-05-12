//
// Created by lifengqiang on 2023/1/30.
//

#include "GLI420Frame.h"

void GLI420Frame::setImageCanvas(int width, int height, int angle) {
    this->width = width;
    this->height = height;
    this->angle = angle;
    lock();
    if (data != nullptr) {
        free(data);
    }
    data = (unsigned char *) malloc(width * height * 3 / 2);
    plane_y = data;
    plane_u = data + width * height;
    plane_v = plane_u + width * height / 4;
    unlock();
}

void GLI420Frame::copyDataByAndroidImage(AndroidImage *image) {
    lock();
    if (width * height == image->getWidth() * image->getHeight()) {
        int *pixel_strides = image->getPixelStride();
        int *row_strides = image->getRowStride();
        auto *buffer = data;
        for (int i = 0; i < height; ++i) {
            int src_offset = row_strides[0] * i;
            memcpy(buffer, image->getY() + src_offset, width);
            buffer += width;
        }
        for (int p = 1; p < 3; p++) {
            unsigned char *plane = image->getPlanes()[p];
            int plane_width = width / 2;
            int plane_height = height / 2;
            int pixel_stride = pixel_strides[p];
            int row_stride = row_strides[p];
            for (int i = 0; i < plane_height; ++i) {
                unsigned char *row_data = plane + row_stride * i;
                for (int j = 0; j < plane_width; ++j) {
                    *(buffer++) = row_data[j * pixel_stride];
                }
            }
        }
    }
    unlock();
}
