package com.lifengqiang.video.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lifengqiang.video.model.entity.User;
import com.lifengqiang.video.model.request.UserAccountData;
import com.lifengqiang.video.model.request.UserInfo;
import com.lifengqiang.video.model.result.UserDetails;

import java.util.List;

public interface UserService extends IService<User> {
    Page<User> queryUserByKey(Integer n, String w);

    void addUserAccountList(List<UserAccountData> list);

    void deleteUserAndUserData(Integer id);

    UserDetails getDetails(Integer id);

    User addNew(String username, String password);

    User updateInfo(Integer id, UserInfo info);
}
