package com.lifengqiang.video.base.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.lifengqiang.video.R;
import com.lifengqiang.video.base.pm.BaseView;

public class CaptionedActivity<V extends BaseView<?>> extends PresenterActivity<V> {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_captioned);
        hideActionBar();
        int statusBarHeight = immersiveStatusBar();
        View empty = addStatusBarFillView(statusBarHeight);
        setStatusBar(true);
        LinearLayout layout = get(R.id.activity_title_layout);
        layout.setBackgroundResource(R.color.default_title_background_color);
        empty.setBackgroundResource(R.color.default_title_background_color);
        click(R.id.activity_back, super::onBackPressed);
    }

    protected void setPageTitle(String text) {
        TextView title = get(R.id.activity_title);
        title.setText(text);
    }

    @Override
    public void setContentView(int layoutResID) {
        FrameLayout layout = get(R.id.child_view_container);
        View.inflate(this, layoutResID, layout);
    }
}
