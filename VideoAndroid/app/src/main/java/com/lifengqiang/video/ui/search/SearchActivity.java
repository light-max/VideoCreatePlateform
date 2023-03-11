package com.lifengqiang.video.ui.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.lifengqiang.video.R;
import com.lifengqiang.video.base.activity.PresenterActivity;

public class SearchActivity extends PresenterActivity<SearchActivityView> {
    private final MutableLiveData<String> searchValue = new MutableLiveData<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        hideActionBar();
        int statusBarHeight = immersiveStatusBar();
        View empty = addStatusBarFillView(statusBarHeight);
        empty.setBackgroundResource(R.color.default_title_background_color);
        setStatusBar(true);
        click(R.id.activity_back, this::finish);
    }

    public static void start(Context context) {
        context.startActivity(new Intent(context, SearchActivity.class));
    }

    public MutableLiveData<String> getSearchValue() {
        return searchValue;
    }
}
