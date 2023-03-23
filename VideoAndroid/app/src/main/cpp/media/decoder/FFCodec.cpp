//
// Created by lifengqiang on 2022/9/12.
//

#include "FFCodec.h"

FFCodec::FFCodec(int type) {
    this->mType = type;
    mPacket = av_packet_alloc();
    mFrame = av_frame_alloc();
    mQueue = new msg::MsgQueue(5);
    int result = pthread_create(&mThread, nullptr, run, this);
    wait();
    if (result == -1) {
        LOGE("JNICodec loop thread create fail");
        delete this;
        return;
    }
}

void *FFCodec::run(void *data) {
    auto *codec = (FFCodec *) data;
    codec->loop();
    return nullptr;
}

void FFCodec::wait() {
    pthread_mutex_lock(&mMut);
    pthread_cond_wait(&mCond, &mMut);
    pthread_mutex_unlock(&mMut);
}

void FFCodec::notify() {
    pthread_mutex_lock(&mMut);
    pthread_cond_signal(&mCond);
    pthread_mutex_unlock(&mMut);
}

void FFCodec::loop() {
    notify();
    while (true) {
        msg::Msg &m = mQueue->pull_not_null();
        int m_type = m.getType();
        if (m_type == msg::type::MediaChange) {
            mIsPlaying = false;
            mIsEnd = true;
            mMediaPrepare = false;
            const char *path = (const char *) m.getData();
            LOGW("media change %s", "path");
            if (_prepareMedia(path) && prepareMedia()) {
                mIsEnd = false;
                mMediaPrepare = true;
                mStartTime = av_gettime();
            }
            mQueue->pop();
        } else if (m_type == msg::type::Goon) {
            _startDecode();
            startDecode();
            mIsPlaying = true;
            mQueue->pop();
            LOGW("goon");
        } else if (m_type == msg::type::Decode) {
            _continueDecode();
        } else if (m_type == msg::type::Pause) {
            _pauseDecode();
            pauseDecode();
            LOGW("pause");
            mIsPlaying = false;
            long pause_time = av_gettime();
            wait();
            long diff = av_gettime() - pause_time;
            mStartTime += diff;
        } else if (m_type == msg::type::Release) {
            _releaseDecoder();
            releaseDecoder();
            LOGW("release");
            break;
        } else if (m_type == msg::type::Seek) {
            int *progress_second = (int *) m.getData();
            seekProgressBySecond(*progress_second);
            LOGW("seek");
        } else {
            LOGW("none");
        }
        if (mQueue->size() > 1) {
            mQueue->pop();
        }
    }
}

bool FFCodec::_prepareMedia(const char *path) {
    if (mFormatContext != nullptr) {
        avformat_close_input(&mFormatContext);
        mFormatContext = nullptr;
    }
    if (mCodecContext != nullptr) {
        if (avcodec_is_open(mCodecContext)) {
            avcodec_close(mCodecContext);
        }
        avcodec_free_context(&mCodecContext);
        mCodecContext = nullptr;
    }

    int result;
    mFormatContext = avformat_alloc_context();
    result = avformat_open_input(&mFormatContext, path, nullptr, nullptr);
    if (result) {
        errorPrint("打开流失败");
        return false;
    }

    result = avformat_find_stream_info(mFormatContext, nullptr);
    if (result < 0) {
        errorPrint("查找流信息失败");
        return false;
    }

    int target_type = mType == CODEC_TYPE_AUDIO
                      ? AVMEDIA_TYPE_AUDIO
                      : AVMEDIA_TYPE_VIDEO;
    int index = -1;
    for (int i = 0; i < mFormatContext->nb_streams; ++i) {
        if (mFormatContext->streams[i]->codecpar->codec_type == target_type) {
            mDuration = mFormatContext->duration;
            mTimeBase = mFormatContext->streams[i]->time_base;
            mDurationSecond = (int) (mDuration / 1000000);
            index = i;
            mTargetStreamIndex = index;
            break;
        }
    }
    if (index == -1) {
        errorPrint("找不到对应媒体的流");
        return false;
    }

    mCodecParameters = mFormatContext->streams[index]->codecpar;
    //获取视频流的解码器
    mCodec = avcodec_find_decoder(mCodecParameters->codec_id);
    //获取解码上下文
    mCodecContext = avcodec_alloc_context3(mCodec);
    //===============配置解码线程相关=========
    mCodecContext->thread_count = 0;
//    if (mCodec->capabilities | AV_CODEC_CAP_FRAME_THREADS)
//        mCodecContext->thread_type = FF_THREAD_FRAME;
//    else if (mCodec->capabilities | AV_CODEC_CAP_SLICE_THREADS)
//        mCodecContext->thread_type = FF_THREAD_SLICE;
//    else
//        mCodecContext->thread_count = 1;
    //===============配置解码线程相关===========
    //将解码器参数复制到解码上下文(因为解码上下文目前还没有解码器参数)
    avcodec_parameters_to_context(mCodecContext, mCodecParameters);
    //启动解码器
    result = avcodec_open2(mCodecContext, mCodec, nullptr);
    if (result) {
        errorPrint("解码器启动失败");
        return false;
    }

    return true;
}

void FFCodec::_startDecode() {
    if (mMediaPrepare && mIsEnd) {
        mIsEnd = false;
        mIsPlaying = true;
        mStartTime = av_gettime();
        mCurrentSecond = -1;
        av_seek_frame(mFormatContext, -1, 0, AVSEEK_FLAG_BACKWARD);
        avcodec_flush_buffers(mCodecContext);
    } else {
        mQueue->decode();
    }
}

void FFCodec::_continueDecode() {
    if (!mMediaPrepare) {
        LOGW("资源未准备就绪");
        mQueue->pause();
        return;
    }
    int result;
    if (!mIsEnd) {
        result = av_read_frame(mFormatContext, mPacket);
        if (result == 0) {
            if (mTargetStreamIndex == mPacket->stream_index) {
                avcodec_send_packet(mCodecContext, mPacket);
            }
        } else {
            mIsEnd = true;
        }
    }
    result = avcodec_receive_frame(mCodecContext, mFrame);
    if (result == 0) {
        if (mIsSeek) {
            mIsSeek = false;
            long pts = (long) (mFrame->pts * av_q2d(mTimeBase) * 1000000.0);
            mStartTime = av_gettime() - pts;
            mCurrentTime = 0;
        }
        continueDecode();
    } else if (result == AVERROR(EAGAIN)) {
        if (mIsEnd) {
            LOGI("decode EOS");
            seekProgressBySecond(0);
            mIsEnd = false;
//            mQueue->pause();
            finishDecode();
        }
    }
    av_packet_unref(mPacket);
    av_frame_unref(mFrame);
}

void FFCodec::_pauseDecode() {
}

void FFCodec::_releaseDecoder() {
}

void FFCodec::seekProgressBySecond(int second) {
    long current_time = (long) second * AV_TIME_BASE;
    av_seek_frame(
            mFormatContext, -1,
            current_time,
            AVSEEK_FLAG_ANY
    );
    avcodec_flush_buffers(mCodecContext);
    mIsSeek = true;
}
