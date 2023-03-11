package com.lifengqiang.video.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.lifengqiang.video.R;
import com.lifengqiang.video.base.activity.FullScreenActivity;
import com.lifengqiang.video.ui.account.login.LoginActivity;
import com.lifengqiang.video.ui.create.record.RecordActivity;
import com.lifengqiang.video.ui.setting.SettingActivity;

public class MainPageActivity extends FullScreenActivity<MainPageView> {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setStatusBar(true);
        click(R.id.create, () -> RecordActivity.startImage(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SettingActivity.LOGOUT && resultCode == RESULT_OK) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra(LoginActivity.auto, false);
            startActivity(intent);
            finish();
        }
    }
}
