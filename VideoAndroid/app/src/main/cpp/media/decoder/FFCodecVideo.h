//
// Created by lifengqiang on 2022/9/15.
//

#ifndef VIDEOUSE_FFCODECVIDEO_H
#define VIDEOUSE_FFCODECVIDEO_H

#include "FFCodec.h"
#include "Mutex.h"
#include "libyuv.h"

namespace state {
    enum {
        PREPARE = 0,
        START,
        PAUSE,
        DECODE,
        RELEASE,
        ERROR,
        FINISH
    };
}

class FFCodecVideo : public FFCodec {
private:
    //==========video===============
    SwsContext *sws = nullptr;
    AVFrame *i420Frame = nullptr;
    //计算出转换为RGB所需要的容器的大小
    //接收的容器
    uint8_t *dst_data[4] = {nullptr};
    //每一行的首地址（R、G、B、A四行）
    int dst_line_size[4];

    bool mIsUseWsw = true;

    void *mListener = nullptr;

    void (*mPrepareCallback)(void *, FFCodecVideo *) = nullptr;

    void (*mPlayCallback)(void *, int, FFCodecVideo *) = nullptr;

    void (*mRenderCallback)(void *, uint8_t *[], int []) = nullptr;

    void printFrameRate() {
        static int frame = 0;
        static long start_time = av_gettime();
        static long current_time = av_gettime();
        static int second = 0;
        long time = av_gettime();
        frame++;
        if ((time - start_time) / 1000000 > (current_time - start_time) / 1000000) {
            current_time = time;
            second++;
            LOGI("decode %d %d", second, frame);
            frame = 0;
        }
    }

public:
    FFCodecVideo() : FFCodec(CODEC_TYPE_VIDEO) {}

    bool prepareMedia() override;

    void startDecode() override;

    void continueDecode() override;

    void pauseDecode() override;

    void releaseDecoder() override;

    void finishDecode() override;

    void setListener(void *listener) { this->mListener = listener; }

    void setPrepareCallback(void (*callback)(void *, FFCodecVideo *)) {
        this->mPrepareCallback = callback;
    }

    void setPlayCallback(void (*callback)(void *, int, FFCodecVideo *)) {
        this->mPlayCallback = callback;
    }

    void setRenderCallback(void (*callback)(void *, uint8_t *[], int[])) {
        this->mRenderCallback = callback;
    }

    int getWidth() {
        return mCodecContext->width;
    }

    int getHeight() {
        return mCodecContext->height;
    }

    void callPlayerCallback() {
        if (mListener == nullptr || mPlayCallback == nullptr) {
            return;
        }
        if (isMediaPrepare()) {
            mPlayCallback(mListener, state::PREPARE, this);
        }
        if (isPlaying()) {
            mPlayCallback(mListener, state::START, this);
        } else {
            mPlayCallback(mListener, state::PAUSE, this);
        }
    }
};


#endif //VIDEOUSE_FFCODECVIDEO_H
