package com.lifengqiang.video.ui.works;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.lifengqiang.video.R;
import com.lifengqiang.video.api.Api;
import com.lifengqiang.video.base.activity.FullScreenActivity;
import com.lifengqiang.video.ui.main.home.view.VideoViewContainer;

public class WorksPlayerActivity extends FullScreenActivity<WorksPlayerView> {
    public static final String WORKS_ID = "works_id";

    private VideoViewContainer videoViewContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar(true);
        setContentView(R.layout.activity_works_player);
        click(R.id.back, this::finish);
        videoViewContainer = get(R.id.video_container);
        Api.getWorks(getIntent().getIntExtra(WORKS_ID, 0))
                .error((message, e) -> toast(message))
                .success(data -> {
                    videoViewContainer.setWorks(data);
                    getIView().setWorksInfo(data);
                }).run();
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoViewContainer.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoViewContainer.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainHandler.postDelayed(() -> {
            videoViewContainer.release();
        }, 100);
    }
}
