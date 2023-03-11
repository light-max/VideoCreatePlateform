package com.lifengqiang.video.base.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.lifengqiang.video.base.pm.BaseView;

public class FullScreenActivity <V extends BaseView<?>> extends PresenterActivity<V> {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideActionBar();
        immersiveStatusBar();
    }
}
