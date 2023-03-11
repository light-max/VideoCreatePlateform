package com.lifengqiang.video.ui.setting.password;

import android.widget.TextView;

import com.lifengqiang.video.R;
import com.lifengqiang.video.base.pm.BaseView;

public class PasswordSetView extends BaseView<PasswordSetActivity> {
    public String getSource() {
        return this.<TextView>get(R.id.source).getText().toString();
    }

    public String getPassword() {
        TextView password = get(R.id.password);
        TextView password1 = get(R.id.password1);
        String[] p = new String[]{
                password.getText().toString(),
                password1.getText().toString()
        };
        if (p[0].equals(p[1])) {
            return p[0];
        }
        return null;
    }
}
