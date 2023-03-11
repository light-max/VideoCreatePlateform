//
// Created by lifengqiang on 2023/1/30.
//

#ifndef VIDEOSHARE_FFMEDIAENCODER_H
#define VIDEOSHARE_FFMEDIAENCODER_H

#include "comm/mylog.h"
#include "ffmpeg/ffc_comm.h"
#include <thread>
#include "comm/ThreadSafeQueue.h"
#include "renderer/image/NativeImage.h"
#include "comm/pfr.h"
#include <unistd.h>
#include "libyuv/libyuv.h"

extern "C" {
#include <libavutil/avassert.h>
};

class AudioFrame {
public:
    AudioFrame(uint8_t *data, int dataSize, bool hardCopy = true) {
        this->dataSize = dataSize;
        this->data = data;
        this->hardCopy = hardCopy;
        if (hardCopy) {
            this->data = static_cast<uint8_t *>(malloc(this->dataSize));
            memcpy(this->data, data, dataSize);
        }
    }

    ~AudioFrame() {
        if (hardCopy && this->data)
            free(this->data);
        this->data = nullptr;
    }

    uint8_t *data = nullptr;
    int dataSize = 0;
    bool hardCopy = true;
};

/**
 * I420
 */
class VideoFrame {
public:
    int width, height, linesize[3];
    unsigned char *buffer = nullptr;
    unsigned char *plane[3] = {nullptr};

    VideoFrame(int width, int height, int width_stride) {
        this->width = width;
        this->height = height;
        linesize[0] = width_stride;
        linesize[1] = width_stride / 2;
        linesize[2] = width_stride / 2;
        buffer = new unsigned char[width_stride * height * 3 / 2];
        plane[0] = buffer;
        plane[1] = plane[0] + width_stride * height;
        plane[2] = plane[1] + width_stride * height / 4;
    }

    ~VideoFrame() {
        delete buffer;
    }
};

class AVStreamWrapper {
public:
    const AVCodec *codec;
    AVStream *stream;
    AVCodecContext *codecCtx;
    AVPacket *packet;
    AVFrame *frame;

    volatile int64_t nextPts = 0;
    volatile int encodeEnd = 0;
    int samplesCount = 0;

    SwrContext *swrContext;
};

class FFMediaEncoder {
private:
    AVFormatContext *mFormatCtx = nullptr;
    const AVOutputFormat *mOutputFormat;
    AVStreamWrapper *mAudioStream;
    AVStreamWrapper *mVideoStream;

    std::thread *mMediaThread = nullptr;
    ThreadSafeQueue<VideoFrame *> mVideoFrameQueue;
    ThreadSafeQueue<AudioFrame *> mAudioFrameQueue;
    volatile bool mExit = false;
private:
    static void StartMediaEncodeThread(FFMediaEncoder *encoder) {
        encoder->mediaEncodeThread();
    }

    void mediaEncodeThread();

    void initVideoStreamWrapper();

    void initAudioStreamWrapper();

public:
    void startEncode(const char *outPath);

    void stopEncode();

    void OnFrameEncode(AudioFrame *inputFrame) {
        if (mExit) return;
        mAudioFrameQueue.Push(inputFrame);
    }

    void OnFrameEncode(NativeImage *inputFrame) {
        if (mExit) return;
        if (mVideoStream == nullptr) return;
        AVFrame *frame = mVideoStream->frame;
        if (frame == nullptr) return;
        auto *image = new VideoFrame(inputFrame->width, inputFrame->height, frame->linesize[0]);
        inputFrame->lock();
        libyuv::ABGRToI420(inputFrame->buffer, inputFrame->linesize,
                           image->plane[0], image->linesize[0],
                           image->plane[1], image->linesize[1],
                           image->plane[2], image->linesize[2],
                           1080, 1920);
        inputFrame->unlock();
        mVideoFrameQueue.Push(image);
    }

public:
    int EncodeAudioFrame(ThreadSafeQueue<AudioFrame *> &queue, AVStreamWrapper *wrapper);

    int EncodeVideoFrame(ThreadSafeQueue<VideoFrame *> &queue, AVStreamWrapper *wrapper);

    ~FFMediaEncoder(){
        LOGI("de");
    }
};


#endif //VIDEOSHARE_FFMEDIAENCODER_H
