//
// Created by lifengqiang on 2022/9/12.
//

#ifndef VIDEOUSE_FFCODEC_H
#define VIDEOUSE_FFCODEC_H

#include "mylog.h"
#include <pthread.h>
#include <mutex>
#include "ffmpeg/ffc_comm.h"
#include "MsgQueue.h"

enum {
    CODEC_TYPE_VIDEO = 1,
    CODEC_TYPE_AUDIO,
};

class FFCodec {
protected:
    pthread_t mThread = 0;
    pthread_mutex_t mMut = PTHREAD_MUTEX_INITIALIZER;
    pthread_cond_t mCond = PTHREAD_COND_INITIALIZER;

    const char *mPath = nullptr;
    int mType;

    msg::MsgQueue *mQueue;

    bool mIsEnd = true;
    bool mMediaPrepare = false;
    bool mIsPlaying = false;
    bool mIsSeek = false;

    //=========ffmpeg object========
    AVFormatContext *mFormatContext = nullptr;
    AVCodecParameters *mCodecParameters = nullptr;
    const AVCodec *mCodec = nullptr;
    AVCodecContext *mCodecContext = nullptr;
    AVPacket *mPacket = nullptr;
    AVFrame *mFrame = nullptr;
    AVRational mTimeBase = {0, 0};

    int mTargetStreamIndex = 0;

    const char *mErrorText = nullptr;
public:
    long mDuration = 0;
    long mStartTime = 0;
    long mCurrentTime = 0;

    int mDurationSecond = 0;
    int mCurrentSecond = -1;

    bool exit = false;

private:
    static void *run(void *);

    void loop();

    void release();

public:
    FFCodec(int type);

    ~FFCodec() {
        exit = true;
        notify();
    }

    void setPath(const char *path) {
        mPath = path;
        mQueue->setMediaPath(path);
        notify();
    }

    const char *getPath() {
        return mPath;
    }

    void goon() {
        mQueue->goon();
        notify();
    }

    void pause() {
        mQueue->pause();
    }

    /**
     * @param progress 秒
     */
    void seekTo(int progress) {
        mQueue->seek(progress);
    }

    void wait();

    void notify();

    //=========解码流程相关方法
    bool _prepareMedia(const char *path);

    void _startDecode();

    void _continueDecode();

    void _pauseDecode();

    void _releaseDecoder();

    void seekProgressBySecond(int second);

    //========子类回调
    virtual bool prepareMedia() = 0;

    virtual void startDecode() = 0;

    virtual void continueDecode() = 0;

    /**
     * 对象构造时会创建线程，此时mQueue为空
     * mQueue为空时调用纯虚函数的pauseDecode会引发异常
     */
    virtual void pauseDecode() {};

    virtual void releaseDecoder() = 0;

    virtual void finishDecode() = 0;

    void errorPrint(const char *msg) {
        LOGE("error: %s", msg);
        mErrorText = msg;
    }

    const char *getErrorText() {
        return mErrorText;
    }

    bool isMediaPrepare() {
        return mMediaPrepare;
    }

    bool isPlaying() {
        return mIsPlaying;
    }

    int getDuration() {
        return mDurationSecond;
    }

    int getProgress() {
        return mCurrentSecond == -1 ? 0 : mCurrentSecond;
    }
};


#endif //VIDEOUSE_FFCODEC_H
