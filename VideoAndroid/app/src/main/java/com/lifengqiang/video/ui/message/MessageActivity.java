package com.lifengqiang.video.ui.message;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.lifengqiang.video.R;
import com.lifengqiang.video.base.activity.CaptionedActivity;
import com.lifengqiang.video.data.observer.UserDetailsData;
import com.lifengqiang.video.data.result.User;
import com.lifengqiang.video.utils.AndroidBug5497Workaround;

public class MessageActivity extends CaptionedActivity<MessageView> {
    public static final String USER_ID = "user_id";
    public static final String USER_NICKNAME = "user_nickname";
    public static final String USER_HEAD = "user_head";

    private User me = new User();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar(true);
        setPageTitle("留言消息");
        setContentView(R.layout.activity_message);
        AndroidBug5497Workaround.assistActivity(this);
        me = UserDetailsData.getInstance().getValue();
        
    }

    public int getUserId() {
        return getIntent().getIntExtra(USER_ID, 0);
    }

    public String getNickname() {
        return getIntent().getStringExtra(USER_NICKNAME);
    }

    public String getUserHead() {
        return getIntent().getStringExtra(USER_HEAD);
    }
}
