package com.lifengqiang.video;

import android.os.Bundle;

import com.lifengqiang.video.api.Api;
import com.lifengqiang.video.base.activity.NoMvpActivity;

public class MainActivity extends NoMvpActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideActionBar();
        Api.login("user1", "1234")
                .before(() -> {
                    System.out.println("之前");
                })
                .after(() -> {
                    System.out.println("之后");
                })
                .error((message, e) -> {
                    System.out.println(message);
                })
                .success(() -> {
                    System.out.println("ok");
                }).run();
    }
}