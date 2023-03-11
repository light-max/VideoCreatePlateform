package com.lifengqiang.video.api;

import com.lifengqiang.video.async.Async;
import com.lifengqiang.video.data.observer.UserDetailsData;
import com.lifengqiang.video.data.request.NewUserInfo;
import com.lifengqiang.video.data.result.User;

import java.io.File;

public class Api {
    public static Async.Builder<User> login(String username, String password) {
        return ExRequestBuilder.post("/user/login")
                .form()
                .field("username", username)
                .field("password", password)
                .<ExRequestBuilder>as()
                .async(User.class)
                .success(UserDetailsData::post);
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

    public static Async.Builder<User> getUserDetails() {
        return ExRequestBuilder.get("/user/details")
                .async(User.class)
                .success(UserDetailsData::post);
    }

    public static Async.Builder<User> setUserInfo(NewUserInfo userInfo) {
        return ExRequestBuilder.put("/user/info")
                .raw()
                .json(userInfo)
                .<ExRequestBuilder>as()
                .async(User.class)
                .success(UserDetailsData::post);
    }

    public static Async.Builder<?> setHead(File file) {
        return ExRequestBuilder.post("/user/head")
                .form()
                .field("file", file)
                .async();
    }

    public static Async.Builder<?> setPassword(String source, String password) {
        return ExRequestBuilder.post("/user/password")
                .form()
                .field("source", source)
                .field("password", password)
                .async();
    }


}
