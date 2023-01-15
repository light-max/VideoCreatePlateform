package com.lifengqiang.video.base.mvp;

import android.os.Bundle;

import com.lifengqiang.video.async.RunToMainThread;
import com.lifengqiang.video.base.call.Base;

public class BaseModel implements MvpLifecycle, RunToMainThread {
    protected Base base;

    @Override
    public void onViewCreated(Base base, Bundle savedInstanceState) {
    }

    @Override
    public void onCreate(Base base, Bundle savedInstanceState) {
        this.base = base;
    }

    @Override
    public void onStart(Base base, Bundle savedInstanceState) {
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onDestroy() {
        base = null;
    }
}
