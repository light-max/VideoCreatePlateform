//
// Created by lifengqiang on 2023/1/30.
//

#ifndef VIDEOSHARE_ANDROIDIMAGE_H
#define VIDEOSHARE_ANDROIDIMAGE_H


class AndroidImage {
private:
    int width = 0, height = 0;
    unsigned char *planes[3] = {nullptr};
    int planes_pixel_stride[3] = {0};
    int planes_row_stride[3] = {0};
    long timestamp_ns = 0;
public:
    void setImageSize(int width, int height);

    void setPlanesBuffer(unsigned char *y, unsigned char *u, unsigned char *v);

    void setPlanesPixelStride(int y, int u, int v);

    void setPlanesRowStride(int y, int u, int v);

    void setTimestampNS(int timestamp_ns);

    int getWidth() { return width; }

    int getHeight() { return height; }

    unsigned char *getY() const { return planes[0]; }

    unsigned char *getU() const { return planes[1]; }

    unsigned char *getV() const { return planes[2]; }

    unsigned char **getPlanes() { return planes; }

    int *getPixelStride() { return planes_pixel_stride; }

    int *getRowStride() { return planes_row_stride; }

    long getTimeStampNS() { return timestamp_ns; }
};


#endif //VIDEOSHARE_ANDROIDIMAGE_H
