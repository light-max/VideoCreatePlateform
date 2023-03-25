package com.lifengqiang.video.ui.follow;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.lifengqiang.video.R;
import com.lifengqiang.video.base.activity.FullScreenActivity;

public class FollowActivity extends FullScreenActivity<FollowView> {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);
        addStatusBarFillView(getStateBarHeight());
        click(R.id.back, this::finish);
    }
}
