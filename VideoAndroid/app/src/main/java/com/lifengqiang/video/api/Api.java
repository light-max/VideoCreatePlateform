package com.lifengqiang.video.api;

import com.lifengqiang.video.async.Async;

import java.io.File;
import java.util.List;

public class Api {
    public static Async.Builder<?> login(String username, String password) {
        return ExRequestBuilder.post("/user/login")
                .form()
                .field("username", username)
                .field("password", password)
                .async();
    }

    public static Async.Builder<?> logout() {
        return ExRequestBuilder.post("/user/logout").async();
    }

    public static Async.Builder<?> register(String username, String password) {
        return ExRequestBuilder.post("/user/register")
                .form()
                .field("username", username)
                .field("password", password)
                .async();
    }


}
