package com.lifengqiang.video.base.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lifengqiang.video.base.pm.BaseView;
import com.lifengqiang.video.base.pm.IPresenter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class PresenterFragment<V extends BaseView<?>> extends Fragment implements IPresenter<V> {
    protected V view;
    protected Map<Object, Object> globalVariableMap;
    protected Handler mainHandler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainHandler = new Handler(Looper.getMainLooper());
        if (createIView()) {
            view.onCreate(getLifecycle(), savedInstanceState);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.view.onViewCreated(view, savedInstanceState);
    }

    public boolean createIView() {
        try {
            ParameterizedType superclass = (ParameterizedType) getClass().getGenericSuperclass();
            assert superclass != null;
            Type[] types = superclass.getActualTypeArguments();
            Class<?> aClass = (Class<?>) types[0];
            Constructor<?> constructor = aClass.getConstructor();
            view = (V) constructor.newInstance();
            Field field = Class.forName(BaseView.class.getName())
                    .getDeclaredField("presenter");
            field.setAccessible(true);
            field.set(view, this);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getStateBarHeight() {
        int resourceId = getResources().getIdentifier("status_bar_height",
                "dimen", "android");
        if (resourceId > 0) {
            return getResources().getDimensionPixelSize(resourceId);
        } else {
            return 64;
        }
    }

    @Override
    public V getIView() {
        return view;
    }

    @Override
    public Map<Object, Object> map() {
        if (globalVariableMap == null) {
            globalVariableMap = new HashMap<>();
        }
        return globalVariableMap;
    }

    public Handler getMainHandler() {
        return mainHandler;
    }
}
