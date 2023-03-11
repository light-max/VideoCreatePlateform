package com.lifengqiang.video.ui.setting.user;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.lifengqiang.video.R;
import com.lifengqiang.video.api.Api;
import com.lifengqiang.video.base.activity.CaptionedActivity;
import com.lifengqiang.video.data.observer.UserDetailsData;
import com.lifengqiang.video.data.request.NewUserInfo;

public class UserInfoActivity extends CaptionedActivity<UserInfoView> {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPageTitle("编辑个人资料");
        setContentView(R.layout.activity_user_info);
        UserDetailsData.ob(this, (user) -> view.setUserData(user));
        click(R.id.save, () -> {
            NewUserInfo info = new NewUserInfo();
            info.setNickname(view.getNickname());
            info.setDes(view.getDes());
            info.setGender(view.getGender());
            info.setBirthday(view.getBirthday());
            info.setUsername(view.getUsername());
            Api.setUserInfo(info)
                    .error((message, e) -> toast(message))
                    .success(() -> {
                        toast("修改成功");
                        onBackPressed();
                    }).run();
        });
    }
}
