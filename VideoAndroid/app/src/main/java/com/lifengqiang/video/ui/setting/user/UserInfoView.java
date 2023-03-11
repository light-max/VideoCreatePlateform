package com.lifengqiang.video.ui.setting.user;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.lifengqiang.video.R;
import com.lifengqiang.video.base.call.ValueCall;
import com.lifengqiang.video.base.pm.BaseView;
import com.lifengqiang.video.data.result.User;

public class UserInfoView extends BaseView<UserInfoActivity> {

    private TextView nickname;
    private TextView des;
    private TextView gender;
    private TextView birthday;
    private TextView username;

    @Override
    public void onCreate(Bundle saveInstanceState) {
        nickname = get(R.id.nickname);
        des = get(R.id.des);
        gender = get(R.id.gender);
        birthday = get(R.id.birthday);
        username = get(R.id.username);
        click(R.id.nickname_layout, () -> {
            editText(getNickname(), (value, e) -> {
                nickname.setText(value);
            });
        });
        click(R.id.des_layout, () -> {
            editText(getDes(), (value, e) -> {
                des.setText(value);
            });
        });
        click(R.id.username_layout, () -> {
            editText(getUsername(), (value, e) -> {
                username.setText(value);
            });
        });
        click(R.id.gender_layout, () -> {
            int checkedItem = getGender().equals("male") ? 0 : 1;
            String[] items = {"男", "女"};
            new AlertDialog.Builder(getContext())
                    .setSingleChoiceItems(items, checkedItem, (dialog, which) -> {
                        gender.setText(items[which]);
                        dialog.dismiss();
                    }).show();
        });
        click(R.id.birthday_layout, () -> {
            editText(getBirthday(), (value, e) -> {
                birthday.setText(value);
            });
        });
    }

    public void setUserData(User user) {
        nickname.setText(user.getNickname());
        des.setText(user.getDes());
        gender.setText("male".equals(user.getGender()) ? "男" : "女");
        birthday.setText(user.getBirthday());
        username.setText(user.getUsername());
    }

    private void editText(String source, ValueCall<String, ?> call) {
        EditText text = new EditText(getContext());
        text.setText(source);
        new AlertDialog.Builder(getContext())
                .setView(text)
                .setTitle("编辑内容")
                .setPositiveButton("确定", (dialog, which) -> {
                    call.call(text.getText().toString(), null);
                }).show();
    }

    public String getNickname() {
        return nickname.getText().toString();
    }

    public String getDes() {
        return des.getText().toString();
    }

    public String getGender() {
        String s = gender.getText().toString();
        return s.equals("男") ? "male" : "female";
    }

    public String getBirthday() {
        return birthday.getText().toString();
    }

    public String getUsername() {
        return username.getText().toString();
    }
}
