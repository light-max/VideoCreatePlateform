//
// Created by lifengqiang on 2022/9/23.
//

#include "FFCodecAudio.h"

bool FFCodecAudio::prepareMedia() {
    if (swr != nullptr) {
        swr_free(&swr);
    }
    in_sample_fmt = mCodecContext->sample_fmt;
    in_sample_rate = mCodecContext->sample_rate;
    in_ch_layout = mCodecContext->channel_layout;
    swr = swr_alloc();
    swr_alloc_set_opts(
            swr,
            (int64_t) out_ch_layout, out_sample_fmt, out_sample_rate,
            (int64_t) in_ch_layout, in_sample_fmt, in_sample_rate,
            0, nullptr);
    swr_init(swr);
    if (mListener != nullptr && mPrepareCallback != nullptr) {
        mPrepareCallback(mListener, this);
    }
    return true;
}

void FFCodecAudio::continueDecode() {
    const auto **in_data = (const unsigned char **) mFrame->data;
    swr_convert(
            swr,
            &pcm_data, out_sample_rate * out_channel_nb,
            in_data, mFrame->nb_samples);
    int size = av_samples_get_buffer_size(
            mFrame->linesize,
            out_channel_nb,
            mFrame->nb_samples,
            out_sample_fmt,
            1);
    if (mListener != nullptr && mRenderCallback != nullptr) {
        mRenderCallback(mListener, pcm_data, size);
    }

    long current_time = av_gettime();
    long diff = current_time - mStartTime;
    long frame_time = (long) (mFrame->pts * 1000000.0 * av_q2d(mTimeBase));
    long s = frame_time - diff;
    if (s >= 0) {
        av_usleep(s);
    }
}


