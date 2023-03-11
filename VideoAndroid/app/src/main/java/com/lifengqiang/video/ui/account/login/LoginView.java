package com.lifengqiang.video.ui.account.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

import com.lifengqiang.video.R;
import com.lifengqiang.video.base.pm.BaseView;
import com.lifengqiang.video.ui.account.register.RegisterActivity;

public class LoginView extends BaseView<LoginActivity> {
    @Override
    public void onCreate(Bundle saveInstanceState) {
        click(R.id.go_register, () -> {
            Intent intent = new Intent(getContext(), RegisterActivity.class);
            getContext().startActivity(intent);
        });
        TextView username = get(R.id.username);
        TextView password = get(R.id.password);
        CheckBox remember = get(R.id.remember);
        CheckBox auto = get(R.id.auto);
        remember.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                auto.setChecked(false);
            }
        });
        auto.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                remember.setChecked(true);
            }
        });
        SharedPreferences sp = presenter.getPreferences(0);
        remember.setChecked(sp.getBoolean("remember", false));
        auto.setChecked(sp.getBoolean("auto", false));
        if (remember.isChecked()) {
            username.setText(sp.getString("username", ""));
            password.setText(sp.getString("password", ""));
        }
        if (auto.isChecked()) {
            presenter.callAutoLogin();
        }
    }

    public String getUsername() {
        return super.<TextView>get(R.id.username).getText().toString();
    }

    public String getPassword() {
        return super.<TextView>get(R.id.password).getText().toString();
    }

    public boolean isRemember() {
        return super.<CheckBox>get(R.id.remember).isChecked();
    }

    public boolean isAuto() {
        return super.<CheckBox>get(R.id.auto).isChecked();
    }

    public void saveState() {
        presenter.getPreferences(0)
                .edit()
                .putString("username", getUsername())
                .putString("password", getPassword())
                .putBoolean("remember", isRemember())
                .putBoolean("auto", isAuto())
                .apply();
    }
}
