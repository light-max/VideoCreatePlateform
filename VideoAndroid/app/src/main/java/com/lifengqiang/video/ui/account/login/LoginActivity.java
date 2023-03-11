package com.lifengqiang.video.ui.account.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.lifengqiang.video.R;
import com.lifengqiang.video.api.Api;
import com.lifengqiang.video.base.activity.FullScreenActivity;
import com.lifengqiang.video.ui.main.MainPageActivity;

public class LoginActivity extends FullScreenActivity<LoginView> {
    public static final String auto = "auto";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        click(R.id.post, () -> {
            view.saveState();
            login();
        });
    }

    public void callAutoLogin() {
        boolean isUseAutoLogin = getIntent().getBooleanExtra(auto, false);
        if (isUseAutoLogin) {
            login();
        }
    }

    public void login() {
        Api.login(view.getUsername(), view.getPassword())
                .error((message, e) -> toast(message))
                .success(() -> {
                    startActivity(new Intent(this, MainPageActivity.class));
                    finish();
                }).run();
    }
}
