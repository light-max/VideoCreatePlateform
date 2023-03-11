//
// Created by lifengqiang on 2023/1/30.
//

#include "FFMediaEncoder.h"

void FFMediaEncoder::startEncode(const char *outPath) {
    int result = 0;
    do {
        /* allocate the output media context */
        avformat_alloc_output_context2(&mFormatCtx, nullptr, nullptr, outPath);
        if (!mFormatCtx) {
            avformat_alloc_output_context2(&mFormatCtx, nullptr, "mpeg", outPath);
        }
        if (!mFormatCtx) {
            result = -1;
            break;
        }
        if (avio_open(&mFormatCtx->pb, outPath, AVIO_FLAG_READ_WRITE) < 0) {
            result = -1;
            break;
        }
        mOutputFormat = mFormatCtx->oformat;
        mVideoStream = new AVStreamWrapper();
        mAudioStream = new AVStreamWrapper();
        if (mOutputFormat->video_codec != AV_CODEC_ID_NONE) {
            initVideoStreamWrapper();
        }
        if (mOutputFormat->audio_codec != AV_CODEC_ID_NONE) {
            initAudioStreamWrapper();
        }
        AVDictionary *opt = nullptr;
        av_dict_set_int(&opt, "video_track_timescale", 30, 0);
        av_dict_set(&opt, "preset", "slow", 0);
        av_dict_set(&opt, "tune", "zerolatency", 0);
        result = avformat_write_header(mFormatCtx, &opt);
        if (result < 0) {
            result = -1;
            break;
        }
    } while (false);
    if (result >= 0) {
        if (mMediaThread == nullptr)
            mMediaThread = new std::thread(StartMediaEncodeThread, this);
    }
}

void FFMediaEncoder::initVideoStreamWrapper() {
    mVideoStream->codec = avcodec_find_encoder(AV_CODEC_ID_MPEG4);
    mVideoStream->stream = avformat_new_stream(mFormatCtx, mVideoStream->codec);
    if (mVideoStream->stream == nullptr) {
        LOGI("新建流失败");
        return;
    }
    mVideoStream->stream->id = mFormatCtx->nb_streams - 1;
    mVideoStream->stream->time_base = (AVRational) {1, 30};

    AVCodecContext *pCodecCtx = avcodec_alloc_context3(mVideoStream->codec);
    pCodecCtx->codec_id = mVideoStream->codec->id;
    pCodecCtx->codec_type = AVMEDIA_TYPE_VIDEO;
    pCodecCtx->pix_fmt = AV_PIX_FMT_YUV420P;
    pCodecCtx->width = 1080;
    pCodecCtx->height = 1920;
    pCodecCtx->time_base = mVideoStream->stream->time_base;
    pCodecCtx->bit_rate = pCodecCtx->width * pCodecCtx->height * 30 * 0.2;
    pCodecCtx->gop_size = 60;
    pCodecCtx->thread_count = 0;

    avcodec_parameters_from_context(mVideoStream->stream->codecpar, pCodecCtx);

    if (avcodec_open2(pCodecCtx, mVideoStream->codec, nullptr) < 0) {
        LOGE("打开编码器失败");
        return;
    }

    mVideoStream->codecCtx = pCodecCtx;

    AVFrame *frame = av_frame_alloc();
    frame->width = 1080;
    frame->height = 1920;
    frame->format = AV_PIX_FMT_YUV420P;
    av_frame_get_buffer(frame, 0);
    mVideoStream->frame = frame;

    int bufferSize = av_image_get_buffer_size(AV_PIX_FMT_YUV420P, 1080, 1920, 1);
    mVideoStream->packet = av_packet_alloc();
    av_new_packet(mVideoStream->packet, bufferSize * 3);
}

void FFMediaEncoder::initAudioStreamWrapper() {
    mAudioStream->codec = avcodec_find_encoder(AV_CODEC_ID_AAC);
    mAudioStream->stream = avformat_new_stream(mFormatCtx, mAudioStream->codec);
    if (mAudioStream->stream == nullptr) {
        LOGI("新建流失败");
        return;
    }
    mAudioStream->stream->id = mFormatCtx->nb_streams - 1;

    AVCodecContext *mCodecContext = avcodec_alloc_context3(mAudioStream->codec);
    mCodecContext->codec_type = AVMEDIA_TYPE_AUDIO;
    mCodecContext->sample_fmt = AV_SAMPLE_FMT_FLTP;//float, planar, 4 字节
    mCodecContext->sample_rate = 44100;
    mCodecContext->channel_layout = AV_CH_LAYOUT_STEREO;
    mCodecContext->channels = av_get_channel_layout_nb_channels(
            mCodecContext->channel_layout);
    mCodecContext->bit_rate = 96000;
    mAudioStream->codecCtx = mCodecContext;

    if (avcodec_open2(mCodecContext, mAudioStream->codec, nullptr) < 0) {
        LOGI("编码器打开失败");
        return;
    }

    avcodec_parameters_from_context(mAudioStream->stream->codecpar, mCodecContext);

    SwrContext *swr = swr_alloc();
    av_opt_set_channel_layout(swr, "in_channel_layout", AV_CH_LAYOUT_STEREO, 0);
    av_opt_set_channel_layout(swr, "out_channel_layout", AV_CH_LAYOUT_STEREO, 0);
    av_opt_set_int(swr, "in_sample_rate", 44100, 0);
    av_opt_set_int(swr, "out_sample_rate", 44100, 0);
    av_opt_set_sample_fmt(swr, "in_sample_fmt", AV_SAMPLE_FMT_S16, 0);
    av_opt_set_sample_fmt(swr, "out_sample_fmt", AV_SAMPLE_FMT_FLTP, 0);
    swr_init(swr);
    mAudioStream->swrContext = swr;

    mAudioStream->packet = av_packet_alloc();
    mAudioStream->frame = av_frame_alloc();
    mAudioStream->frame->nb_samples = mCodecContext->frame_size;
    mAudioStream->frame->format = mCodecContext->sample_fmt;
    mAudioStream->frame->channel_layout = mCodecContext->channel_layout;
    av_frame_get_buffer(mAudioStream->frame, 0);
}


void FFMediaEncoder::stopEncode() {
    mExit = true;
    mMediaThread->join();
    delete mMediaThread;
    while (!mVideoFrameQueue.Empty()) {
        VideoFrame *pImage = mVideoFrameQueue.Pop();
        delete pImage;
    }
    while (!mAudioFrameQueue.Empty()) {
        AudioFrame *pAudio = mAudioFrameQueue.Pop();
        delete pAudio;
    }
    av_write_trailer(mFormatCtx);
    avcodec_close(mAudioStream->codecCtx);
    avcodec_close(mVideoStream->codecCtx);
    avcodec_free_context(&mAudioStream->codecCtx);
    avcodec_free_context(&mVideoStream->codecCtx);
    avio_close(mFormatCtx->pb);
    avformat_free_context(mFormatCtx);
    swr_free(&mAudioStream->swrContext);
    av_frame_free(&mAudioStream->frame);
    av_frame_free(&mVideoStream->frame);
    av_packet_free(&mAudioStream->packet);
    av_packet_free(&mVideoStream->packet);
}

void FFMediaEncoder::mediaEncodeThread() {
    AVStreamWrapper *vs = mVideoStream;
    AVStreamWrapper *as = mAudioStream;
    while (!vs->encodeEnd || !as->encodeEnd) {
        double videoTimestamp = vs->nextPts * av_q2d(vs->codecCtx->time_base);
        double audioTimestamp = as->nextPts * av_q2d(as->codecCtx->time_base);
        if (!vs->encodeEnd &&
            (as->encodeEnd || av_compare_ts(vs->nextPts, vs->codecCtx->time_base,
                                            as->nextPts, as->codecCtx->time_base) <= 0)) {
            //视频和音频时间戳对齐，人对于声音比较敏感，防止出现视频声音播放结束画面还没结束的情况
            if (audioTimestamp <= videoTimestamp && as->encodeEnd) vs->encodeEnd = 1;
            vs->encodeEnd = EncodeVideoFrame(mVideoFrameQueue, vs);
        } else {
            as->encodeEnd = EncodeAudioFrame(mAudioFrameQueue, as);
        }
    }
}

int
FFMediaEncoder::EncodeAudioFrame(ThreadSafeQueue<AudioFrame *> &queue, AVStreamWrapper *wrapper) {
    while (queue.Empty() && !mExit) {
        usleep(10 * 1000);
    }
    int result = 0;
    AudioFrame *data = queue.Pop();
    AVFrame *frame = wrapper->frame;
    AVPacket *packet = wrapper->packet;
    AVCodecContext *codecCtx = wrapper->codecCtx;
    if (data != nullptr) {
        wrapper->nextPts += frame->nb_samples;
        result = swr_convert(
                wrapper->swrContext, frame->data, frame->nb_samples,
                (const uint8_t **) &(data->data), data->dataSize / 4);
        delete data;
    }
    if (result < 0) {
        return 1;
    }
    if ((queue.Empty() && mExit) || wrapper->encodeEnd) {
        frame = nullptr;
    }
    if (frame != nullptr) {
        if (av_frame_make_writable(frame) < 0) {
            return 1;
        }
        frame->pts = av_rescale_q(wrapper->samplesCount,
                                  (AVRational) {1, codecCtx->sample_rate}, codecCtx->time_base);
        wrapper->samplesCount += frame->nb_samples;
    }
    result = avcodec_send_frame(codecCtx, frame);
    if (result == AVERROR_EOF) {
        return 1;
    } else if (result < 0) {
        return 0;
    }
    while (!result) {
        result = avcodec_receive_packet(codecCtx, packet);
        if (result == AVERROR(EAGAIN) || result == AVERROR_EOF) {
            return 0;
        } else if (result < 0) {
            return 0;
        }
        av_packet_rescale_ts(packet, codecCtx->time_base, wrapper->stream->time_base);
        packet->stream_index = wrapper->stream->index;
        result = av_interleaved_write_frame(mFormatCtx, packet);
        av_packet_unref(packet);
        if (result < 0) {
            return 0;
        }
    }
    return 0;
}

int
FFMediaEncoder::EncodeVideoFrame(ThreadSafeQueue<VideoFrame *> &queue, AVStreamWrapper *wrapper) {
    while (queue.Empty() && !mExit) {
        usleep(10 * 1000);
    }
    VideoFrame *data = queue.Pop();
    AVFrame *frame = wrapper->frame;
    AVPacket *packet = wrapper->packet;
    AVCodecContext *codecCtx = wrapper->codecCtx;
    if ((queue.Empty() && mExit) || wrapper->encodeEnd) {
        frame = nullptr;
    }
    if (data != nullptr) {
        if (frame != nullptr) {
            if (av_frame_make_writable(frame) < 0) {
//            return 0;
            }
            memcpy(frame->data[0], data->plane[0], data->linesize[0] * data->height);
            memcpy(frame->data[1], data->plane[1], data->linesize[1] * data->height / 2);
            memcpy(frame->data[2], data->plane[2], data->linesize[1] * data->height / 2);
            frame->pts = wrapper->nextPts++;
        }
        delete data;
    }
    int result = avcodec_send_frame(codecCtx, frame);
    if (result == AVERROR_EOF) {
        return 1;
    } else if (result < 0) {
        return 0;
    }
    while (!result) {
        result = avcodec_receive_packet(codecCtx, packet);
        if (result == AVERROR(EAGAIN) || result == AVERROR_EOF) {
            return 0;
        } else if (result < 0) {
            return 0;
        }
        av_packet_rescale_ts(packet, codecCtx->time_base, wrapper->stream->time_base);
        packet->stream_index = wrapper->stream->index;
        result = av_interleaved_write_frame(mFormatCtx, packet);
        av_packet_unref(packet);
        if (result < 0) {
            return 0;
        }
    }
    return result;
}


