//
// Created by lifengqiang on 2022/9/12.
//

#include "FFMediaPlayer.h"

void FFMediaPlayer::vPrepareCallback(void *thiz, FFCodecVideo *codec) {
    auto *player = (FFMediaPlayer *) thiz;
    player->mFrameBuffer.lock();
    player->mFrameBuffer.width = codec->getWidth();
    player->mFrameBuffer.height = codec->getHeight();
    player->mFrameBuffer.createData();
    player->mFrameBuffer.unlock();
    player->prepare = true;
}

void FFMediaPlayer::vPlayCallback(void *thiz, int state, FFCodecVideo *codec) {
    auto *player = (FFMediaPlayer *) thiz;
    player->mVPCMtx.lock();
    if (player->mVideoPlayerCallback != nullptr) {
        switch (state) {
            case state::PREPARE:
                player->mVideoPlayerCallback->prepare(
                        codec->getPath(),
                        codec->getDuration(),
                        codec->getWidth(),
                        codec->getHeight());
                break;
            case state::START:
                player->mVideoPlayerCallback->goon();
                break;
            case state::PAUSE:
                player->mVideoPlayerCallback->pause();
                break;
            case state::DECODE:
                player->mVideoPlayerCallback->progress(
                        codec->getDuration(),
                        codec->getProgress()
                );
                break;
            case state::RELEASE:
                player->mVideoPlayerCallback->release();
                break;
            case state::ERROR:
                player->mVideoPlayerCallback->error(codec->getErrorText());
                break;
            case state::FINISH:
                player->mVideoPlayerCallback->finish();
                break;
        }
    }
    player->mVPCMtx.unlock();
}

void FFMediaPlayer::vRenderCallback(void *thiz, uint8_t **data, int *size) {
    auto *player = (FFMediaPlayer *) thiz;
    NativeFrameBuffer *buffer = &player->mFrameBuffer;
    buffer->lock();
    if (buffer->hasData()) {
        int dst_line_size = buffer->width * 4;
        unsigned char *dst = buffer->data;
        unsigned char *src = data[0];
        if (dst_line_size != size[0]) {
            for (int i = 0; i < buffer->height; ++i) {
                dst += dst_line_size;
                src += size[0];
                memcpy(dst, src, dst_line_size);
            }
        } else {
            memcpy(dst, src, dst_line_size * buffer->height);
        }
    }
    buffer->unlock();

    player->mGSVMtx.lock();
    if (player->mGLSurfaceView != nullptr) {
        player->mGLSurfaceView->render();
    }
    player->mGSVMtx.unlock();
}
