package com.lifengqiang.video.ui.account.register;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.lifengqiang.video.R;
import com.lifengqiang.video.api.Api;
import com.lifengqiang.video.base.activity.CaptionedActivity;

public class RegisterActivity extends CaptionedActivity<RegisterView> {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPageTitle("账号注册");
        setContentView(R.layout.activity_register);
        click(R.id.post, () -> {
            Api.register(view.getUsername(), view.getPassword())
                    .error((message, e) -> toast(message))
                    .success(() -> {
                        toast("注册成功, 快去登录吧");
                        finish();
                    }).run();
        });
    }
}
