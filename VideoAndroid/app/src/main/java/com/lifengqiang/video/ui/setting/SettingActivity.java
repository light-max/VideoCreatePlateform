package com.lifengqiang.video.ui.setting;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.lifengqiang.video.R;
import com.lifengqiang.video.api.Api;
import com.lifengqiang.video.base.activity.CaptionedActivity;
import com.lifengqiang.video.base.pm.BaseView;
import com.lifengqiang.video.ui.setting.head.HeadSetActivity;
import com.lifengqiang.video.ui.setting.password.PasswordSetActivity;
import com.lifengqiang.video.ui.setting.user.UserInfoActivity;

public class SettingActivity extends CaptionedActivity<BaseView<?>> {
    public static final int LOGOUT = 0xFF01;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPageTitle("设置");
        setContentView(R.layout.activity_setting);
        click(R.id.head, () -> startActivity(HeadSetActivity.class));
        click(R.id.info, () -> startActivity(UserInfoActivity.class));
        click(R.id.password, () -> startActivity(PasswordSetActivity.class));
        click(R.id.logout, () -> new AlertDialog.Builder(this)
                .setMessage("你确定要退出已登录的账号吗?")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", (dialog, which) -> {
                    Api.logout().run();
                    setResult(RESULT_OK);
                    finish();
                }).show());
        click(R.id.cache, () -> {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("清理缓存")
                    .setMessage("清理中...")
                    .setCancelable(false)
                    .show();
            mainHandler.postDelayed(() -> {
                dialog.dismiss();
                toast("清理成功");
            }, 1000);
        });
    }

    private void startActivity(Class<?> aClass) {
        Intent intent = new Intent(this, aClass);
        startActivity(intent);
    }
}
