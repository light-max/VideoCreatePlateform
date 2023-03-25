//
// Created by lifengqiang on 2022/9/12.
//

#ifndef VIDEOUSE_SOFTWAREPLAYER_H
#define VIDEOUSE_SOFTWAREPLAYER_H

#include "media/decoder/FFCodecVideo.h"
#include "media/decoder/FFCodecAudio.h"
#include "Mutex.h"
#include "interfaces/OnVideoPlayerCallback.h"
#include "renderer/image/NativeFrameBuffer.h"
#include "interfaces/JGLSurfaceView.h"
#include "interfaces/JAudioTrack.h"

class FFMediaPlayer {
private:
    const char *mPath = nullptr;
    FFCodecVideo *mVideo;

    FFCodecAudio *mAudio;

    OnVideoPlayerCallback *mVideoPlayerCallback = nullptr;
    Mutex mVPCMtx;

    JGLSurfaceView *mGLSurfaceView = nullptr;
    Mutex mGSVMtx;

    JAudioTrack *mAudioTrack = nullptr;
    Mutex mATMtx;

    NativeFrameBuffer mFrameBuffer;

    bool prepare = false;

    static void vPrepareCallback(void *thiz, FFCodecVideo *codec);

    static void vPlayCallback(void *thiz, int state, FFCodecVideo *codec);

    static void vRenderCallback(void *thiz, uint8_t *data[], int size[]);

    static void aPrepareCallback(void *thiz, FFCodecAudio *codec) {
        auto *player = (FFMediaPlayer *) thiz;
    }

    static void aRenderCallback(void *thiz, uint8_t *data, int size) {
        auto *player = (FFMediaPlayer *) thiz;
        player->mATMtx.lock();
        if (player->mAudioTrack != nullptr) {
            player->mAudioTrack->write(data, size);
        }
        player->mATMtx.unlock();
    }

public:
    FFMediaPlayer() {
        mVideo = new FFCodecVideo();
        mVideo->setListener(this);
        mVideo->setPrepareCallback(vPrepareCallback);
        mVideo->setPlayCallback(vPlayCallback);
        mVideo->setRenderCallback(vRenderCallback);

        mAudio = new FFCodecAudio();
        mAudio->mListener = this;
        mAudio->mPrepareCallback = aPrepareCallback;
        mAudio->mRenderCallback = aRenderCallback;
    }

    ~FFMediaPlayer() {
        delete mVideoPlayerCallback;
        mVideoPlayerCallback = nullptr;
        delete mGLSurfaceView;
        mGLSurfaceView = nullptr;
        delete mAudioTrack;
        mAudioTrack = nullptr;
        delete mPath;
        mPath = nullptr;
        mAudio->exit = true;
        mAudio->exit = true;
//        delete mVideo;
        mVideo = nullptr;
//        delete mAudio;
        mAudio = nullptr;
    }

    void setPath(const char *path) {
        this->mPath = path;
        mVideo->setPath(path);
        mAudio->setPath(path);
        prepare = false;
    }

    void goon() {
        mVideo->goon();
        mAudio->goon();
    }

    void pause() {
        mVideo->pause();
        mAudio->pause();
    }

    void toggle() {
        if (mVideo->isPlaying()) {
            pause();
        } else {
            goon();
        }
    }

    void seekTo(int progress) {
        mVideo->seekTo(progress);
        mAudio->seekTo(progress);
    }

    void resume() {
    }

    NativeFrameBuffer *getFrameBuffer() {
        return &mFrameBuffer;
    }

    void registerPlayerCallback(OnVideoPlayerCallback *callback) {
        mVPCMtx.lock();
        if (mVideoPlayerCallback != nullptr) {
            delete mVideoPlayerCallback;
        }
        mVideoPlayerCallback = callback;
        mVPCMtx.unlock();
        mVideo->callPlayerCallback();
    }

    void setGLSurfaceView(JGLSurfaceView *surfaceView) {
        mGSVMtx.lock();
        if (mGLSurfaceView != nullptr) {
            delete mGLSurfaceView;
        }
        mGLSurfaceView = surfaceView;
        mGSVMtx.unlock();
        surfaceView->render();
    }

    void setAudioTrack(JAudioTrack *audoTrack) {
        mATMtx.lock();
        if (mAudioTrack != nullptr) {
            delete mAudioTrack;
        }
        mAudioTrack = audoTrack;
        mATMtx.unlock();
    }

    void waitPrepare() {
        while (!prepare) {
            av_usleep(30000);
        }
    }

    void clearFrameBuffer() {
        mFrameBuffer.lock();
        mFrameBuffer.clearData();
        mFrameBuffer.unlock();
    }
};


#endif //VIDEOUSE_SOFTWAREPLAYER_H
