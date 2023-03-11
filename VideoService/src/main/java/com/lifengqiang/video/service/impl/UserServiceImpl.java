package com.lifengqiang.video.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lifengqiang.video.constant.AssertException;
import com.lifengqiang.video.constant.GlobalConstant;
import com.lifengqiang.video.fieldcheck.FieldCheckException;
import com.lifengqiang.video.mapper.UserMapper;
import com.lifengqiang.video.model.entity.User;
import com.lifengqiang.video.model.request.UserAccountData;
import com.lifengqiang.video.model.request.UserInfo;
import com.lifengqiang.video.model.result.UserDetails;
import com.lifengqiang.video.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Override
    public Page<User> queryUserByKey(Integer n, String w) {
        LambdaQueryWrapper<User> wrapper = new QueryWrapper<User>()
                .lambda();
        if (w != null && !w.trim().isEmpty()) {
            wrapper.like(User::getNickname, w)
                    .or()
                    .like(User::getUsername, w);
        }
        return page(new Page<>(n == null ? 1 : n, 10), wrapper);
    }

    @Override
    public void addUserAccountList(List<UserAccountData> list) {
        List<User> users = new ArrayList<>();
        for (UserAccountData accountData : list) {
            try {
                users.add(User.builder()
                        .username(accountData.getUsername())
                        .nickname(accountData.getNickname())
                        .password(accountData.getPassword())
                        .gender(accountData.getGender())
                        .build()
                        .check());
            } catch (FieldCheckException e) {
                throw new AssertException("序号" + accountData.getIndex() + "的数据发生了异常: " + e.getMessage());
            }
        }
        saveBatch(users);
    }

    @Override
    public void deleteUserAndUserData(Integer id) {
        removeById(id);
    }

    @Override
    public UserDetails getDetails(Integer id) {
        User user = getById(id);
        GlobalConstant.dataNotExists.notNull(user);
        LocalDate birthday = LocalDate.parse(user.getBirthday(), User.BIRTHDAY_FORMAT);
        LocalDate now = LocalDate.now(ZoneId.of("GMT+8"));
        return UserDetails.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .des(user.getDes())
                .gender(user.getGender())
                .birthday(user.getBirthday())
                .age(now.getYear() - birthday.getYear())
                .head("/head/" + user.getId())
                .friend(false)
                .videoCount(0)
                .followCount(0)
                .followerCount(0)
                .friendCount(0)
                .build();
    }

    @Override
    public User addNew(String username, String password) {
        User user = getOne(new QueryWrapper<User>()
                .lambda()
                .eq(User::getUsername, username));
        GlobalConstant.usernameExists.isNull(user);
        user = User.builder()
                .username(username)
                .password(password)
                .build()
                .check();
        save(user);
        return getById(user.getId());
    }

    @Override
    public User updateInfo(Integer id, UserInfo info) {
        User user = getOne(new QueryWrapper<User>()
                .lambda()
                .eq(User::getUsername, info.getUsername()));
        GlobalConstant.usernameExists.isFalse(user != null && !user.getId().equals(id));
        User newUser = User.builder()
                .id(id)
                .nickname(info.getNickname())
                .des(info.getDes())
                .gender(info.getGender())
                .birthday(info.getBirthday())
                .username(info.getUsername())
                .build()
                .check();
        updateById(newUser);
        return newUser;
    }
}
