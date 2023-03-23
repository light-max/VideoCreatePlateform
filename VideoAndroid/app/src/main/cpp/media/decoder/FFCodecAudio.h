//
// Created by lifengqiang on 2022/9/23.
//

#ifndef VIDEOUSE_FFCODECAUDIO_H
#define VIDEOUSE_FFCODECAUDIO_H

#include "FFCodec.h"

class FFCodecAudio : public FFCodec {
private:
    SwrContext *swr = nullptr;
    uint8_t *pcm_data;
public:
    AVSampleFormat in_sample_fmt;
    AVSampleFormat out_sample_fmt;
    int in_sample_rate;
    int out_sample_rate;
    uint64_t in_ch_layout;
    uint64_t out_ch_layout;
    int out_channel_nb;

    void *mListener = nullptr;

    void (*mPrepareCallback)(void *, FFCodecAudio *) = nullptr;

    void (*mRenderCallback)(void *, uint8_t *data, int size) = nullptr;

public:
    FFCodecAudio() : FFCodec(CODEC_TYPE_AUDIO) {
        out_sample_fmt = AV_SAMPLE_FMT_S16;
        out_sample_rate = 44100;
        out_ch_layout = AV_CH_LAYOUT_STEREO;
        out_channel_nb = av_get_channel_layout_nb_channels(out_ch_layout);
        pcm_data = (uint8_t *) av_malloc(out_sample_rate * out_channel_nb);
    }

    bool prepareMedia() override;

    void startDecode() override {};

    void continueDecode() override;

    void pauseDecode() override {};

    void releaseDecoder() override {};

    void finishDecode() override {
        LOGI("audio finish");
    };

    int getFrameSize(){
        return mCodecContext->frame_size;
    }
};


#endif //VIDEOUSE_FFCODECAUDIO_H
