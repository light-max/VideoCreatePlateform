package com.lifengqiang.video.ui.account.register;

import android.widget.TextView;

import com.lifengqiang.video.R;
import com.lifengqiang.video.base.pm.BaseView;

public class RegisterView extends BaseView<RegisterActivity> {
    public String getUsername() {
        return super.<TextView>get(R.id.username).getText().toString();
    }

    public String getPassword() {
        TextView p1 = get(R.id.password);
        TextView p2 = get(R.id.password1);
        String[] p = new String[]{
                p1.getText().toString(),
                p2.getText().toString()
        };
        if (p[0].equals(p[1])) {
            return p[0];
        } else {
            return null;
        }
    }
}
