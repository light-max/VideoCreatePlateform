package com.lifengqiang.video.base.pm;

import android.os.Bundle;
import android.view.View;

import com.lifengqiang.video.base.call.Base;

public abstract class BaseView<Presenter extends IPresenter<?>> implements IView<Presenter>, Base {
    protected Presenter presenter;

    @Override
    public Presenter getPresenter() {
        return presenter;
    }

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void onViewCreated(View view, Bundle saveInstanceState) {
    }
}
