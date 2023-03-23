//
// Created by lifengqiang on 2022/9/15.
//

#include "FFCodecVideo.h"

bool FFCodecVideo::prepareMedia() {
    if (sws != nullptr) {
        sws_freeContext(sws);
        sws = nullptr;
    }
    for (int i = 0; i < 4; ++i) {
        if (dst_data[i] != nullptr) {
            free(dst_data[i]);
            dst_data[i] = nullptr;
        }
    }
    if (i420Frame != nullptr) {
        av_frame_free(&i420Frame);
    }
    //获取frame转RGBA的转换器
    if (mCodecContext->pix_fmt != AV_PIX_FMT_YUV420P) {
        sws = sws_getContext(mCodecContext->width, mCodecContext->height, mCodecContext->pix_fmt,
                             mCodecContext->width, mCodecContext->height, AV_PIX_FMT_YUV420P,
                             SWS_BILINEAR,
                             nullptr, nullptr, nullptr);
        i420Frame = av_frame_alloc();
        i420Frame->width = getWidth();
        i420Frame->height = getHeight();
        i420Frame->format = AV_PIX_FMT_YUV420P;
        av_frame_get_buffer(i420Frame, 0);
        mIsUseWsw = true;
    } else {
        mIsUseWsw = false;
    }

    av_image_alloc(dst_data, dst_line_size,
                   mCodecContext->width, mCodecContext->height,
                   AV_PIX_FMT_RGBA, 1);

    if (mListener != nullptr && mPrepareCallback != nullptr) {
        mPrepareCallback(mListener, this);
    }

//    mMediaPrepare = true;
//    _continueDecode();
//
    if (mListener != nullptr && mPlayCallback != nullptr) {
        mPlayCallback(mListener, state::PREPARE, this);
    }

    return true;
}

void FFCodecVideo::startDecode() {
    if (mListener != nullptr && mPlayCallback != nullptr) {
        mPlayCallback(mListener, state::START, this);
    }
}

void FFCodecVideo::continueDecode() {
    AVFrame* src;
    if (mIsUseWsw) {
        sws_scale(sws, mFrame->data, mFrame->linesize, 0, mFrame->height,
                  i420Frame->data, i420Frame->linesize);
        src = i420Frame;
    } else {
        src = mFrame;
    }
    libyuv::I420ToABGR(
            src->data[0],
            src->linesize[0],
            src->data[1],
            src->linesize[1],
            src->data[2],
            src->linesize[2],
            dst_data[0],
            dst_line_size[0],
            getWidth(),
            getHeight()
    );
    if (mListener != nullptr && mRenderCallback != nullptr) {
        mRenderCallback(mListener, dst_data, dst_line_size);
    }

    long current_time = av_gettime();
    long diff = current_time - mStartTime;
    long frame_time = (long) (mFrame->pts * 1000000.0 * av_q2d(mTimeBase));
    long s = frame_time - diff;
    if (s >= 0) {
        av_usleep(s);
    }

//    long time_diff = abs(current_time - mCurrentTime);
//    if (time_diff >= 1000000)
    int current_second = (int) (frame_time / 1000000);
    if (mCurrentSecond != current_second) {
        mCurrentTime = frame_time;
//        mCurrentSecond = (int) ((mCurrentTime - mStartTime) / 1000000);
        mCurrentSecond = current_second;
        if (mListener != nullptr && mPlayCallback != nullptr) {
            mPlayCallback(mListener, state::DECODE, this);
        }
    }
}

void FFCodecVideo::pauseDecode() {
    if (mListener != nullptr && mPlayCallback != nullptr) {
        mPlayCallback(mListener, state::PAUSE, this);
    }
}

void FFCodecVideo::releaseDecoder() {
    if (sws != nullptr) {
        sws_freeContext(sws);
        sws = nullptr;
    }
    for (int i = 0; i < 4; ++i) {
        if (dst_data[i] != nullptr) {
            free(dst_data[i]);
            dst_data[i] = nullptr;
        }
    }
    if (i420Frame != nullptr) {
        av_frame_free(&i420Frame);
    }
    if (mListener != nullptr && mPlayCallback != nullptr) {
        mPlayCallback(mListener, state::RELEASE, this);
    }
}

void FFCodecVideo::finishDecode() {
    if (mListener != nullptr && mPlayCallback != nullptr) {
        mPlayCallback(mListener, state::FINISH, this);
    }
}

