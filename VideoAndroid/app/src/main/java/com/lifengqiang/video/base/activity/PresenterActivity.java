package com.lifengqiang.video.base.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lifengqiang.video.base.pm.BaseView;
import com.lifengqiang.video.base.pm.IPresenter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class PresenterActivity<V extends BaseView<?>> extends AppCompatActivity
        implements IPresenter<V> {
    protected V view;
    protected Map<Object, Object> globalVariableMap;
    protected Handler mainHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainHandler = new Handler(Looper.getMainLooper());
        if (createIView()) {
            view.onCreate(getLifecycle(), savedInstanceState);
        }
    }

    @Override
    public V getIView() {
        return view;
    }

    public boolean createIView() {
        try {
            ParameterizedType superclass = (ParameterizedType) getClass().getGenericSuperclass();
            assert superclass != null;
            Type[] types = superclass.getActualTypeArguments();
            if (types.length == 0) {
                return false;
            }
            if (!(types[0] instanceof Class<?>)) {
                return false;
            }
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

    public void hideActionBar() {
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    public void hideStatusBar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public int immersiveStatusBar() {
        Window window = getWindow();
        View decorView = window.getDecorView();
//        decorView.setSystemUiVisibility(android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
//                | android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.setStatusBarColor(Color.TRANSPARENT);
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        return getStateBarHeight();
    }

    public void setStatusBar(boolean dark) {
        Window window = getWindow();
        View decorView = window.getDecorView();
        if (dark) {
            window.setStatusBarColor(Color.TRANSPARENT);
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else {
            window.setStatusBarColor(Color.TRANSPARENT);
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    protected View addStatusBarFillView(int statusBarHeight) {
        View rootView = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        View empty = new View(getContext());
        empty.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight));
        ((ViewGroup) rootView).addView(empty, 0);
        return empty;
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
    public Map<Object, Object> map() {
        if (globalVariableMap == null) {
            globalVariableMap = new HashMap<>();
        }
        return globalVariableMap;
    }
}
