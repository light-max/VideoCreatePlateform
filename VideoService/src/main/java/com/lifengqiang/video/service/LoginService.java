package com.lifengqiang.video.service;

import com.lifengqiang.video.model.data.Result;
import com.lifengqiang.video.model.entity.Admin;
import com.lifengqiang.video.model.entity.User;

public interface LoginService {
    Result<Admin> admin(String username, String password);

    Result<User> user(String username, String password);
}
