package com.lifengqiang.video.ui.setting.password;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.lifengqiang.video.R;
import com.lifengqiang.video.api.Api;
import com.lifengqiang.video.base.activity.CaptionedActivity;

public class PasswordSetActivity extends CaptionedActivity<PasswordSetView> {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPageTitle("修改密码");
        setContentView(R.layout.activity_password_set);
        click(R.id.post, () -> {
            if (view.getPassword() == null) {
                toast("两次输入的密码不一致");
                return;
            }
            Api.setPassword(view.getSource(), view.getPassword())
                    .error((message, e) -> toast(message))
                    .success(() -> {
                        toast("修改成功");
                        finish();
                    }).run();
        });
    }
}
