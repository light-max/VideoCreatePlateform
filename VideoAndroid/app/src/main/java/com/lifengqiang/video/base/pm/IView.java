package com.lifengqiang.video.base.pm;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.lifengqiang.video.base.call.Base;

import java.util.Map;

public interface IView<Presenter extends IPresenter<?>> extends Base {
    Presenter getPresenter();

    default void onCreate(Lifecycle lifecycle, Bundle savedInstanceState) {
        lifecycle.addObserver(new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                switch (event) {
                    case ON_CREATE:
                        onCreate(savedInstanceState);
                        break;
                    case ON_START:
                        onStart();
                        break;
                    case ON_RESUME:
                        onResume();
                        break;
                    case ON_PAUSE:
                        onPause();
                        break;
                    case ON_STOP:
                        onStop();
                        break;
                    case ON_DESTROY:
                        lifecycle.removeObserver(this);
                        onDestroy();
                        break;
                }
            }
        });
    }

    void onCreate(Bundle saveInstanceState);

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();

    /**
     * 在fragment中回调
     */
    void onViewCreated(View view, Bundle saveInstanceState);

    //==委托实现
    @Override
    default Context getContext() {
        return getPresenter().getContext();
    }

    @Override
    default FragmentActivity getActivity() {
        return getPresenter().getActivity();
    }

    @Override
    default Fragment getFragment() {
        return getPresenter().getFragment();
    }

    @NonNull
    @Override
    default Lifecycle getLifecycle() {
        return getPresenter().getLifecycle();
    }

    @Override
    default Map<Object, Object> map() {
        return getPresenter().map();
    }

    @Override
    default <T extends View> T get(int viewId) {
        return getPresenter().get(viewId);
    }

    //==委托实现
}
