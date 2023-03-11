package com.lifengqiang.video.ui.create.submit.video;

import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.EditText;

import com.lifengqiang.video.R;
import com.lifengqiang.video.base.pm.BaseView;

public class SubmitVideoView extends BaseView<SubmitVideoActivity> {
    private SurfaceView surfaceView;
    private EditText content;

    @Override
    public void onCreate(Bundle saveInstanceState) {
        surfaceView = get(R.id.surface);
        content = get(R.id.content);
        getSurfaceView().getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (presenter.thread != null) {
                    try {
                        presenter.thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                presenter.thread = new Thread(() -> {
                    presenter.playVideo(presenter.getPath(), holder.getSurface());
                });
                presenter.thread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                presenter.thread.interrupt();
            }
        });
    }

    public SurfaceView getSurfaceView() {
        return surfaceView;
    }

    public EditText getContent() {
        return content;
    }
}
