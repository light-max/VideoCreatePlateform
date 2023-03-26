package com.lifengqiang.video.ui.uspace;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.lifengqiang.video.R;
import com.lifengqiang.video.api.Api;
import com.lifengqiang.video.base.activity.FullScreenActivity;

public class UserSpaceActivity extends FullScreenActivity<UserSpaceView> {
    public static final String USER_ID = "user_id";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar(true);
        setContentView(R.layout.activity_user_space);
        Api.getUserDetails(getTargetUserId()).success(data -> {
            getIView().setUserData(data);
        }).run();
        Api.getUserWorksList(getTargetUserId()).success(data -> {
            getIView().getAdapter().setData(data);
            getIView().getAdapter().notifyDataSetChanged();
            getIView().getWorksCount().setText("共" + data.size() + "个作品");
        }).run();
    }

    public int getTargetUserId() {
        return getIntent().getIntExtra(USER_ID, 0);
    }
}
