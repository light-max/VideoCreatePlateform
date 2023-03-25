package com.lifengqiang.video.controller.api;

import com.lifengqiang.video.model.data.Result;
import com.lifengqiang.video.model.entity.User;
import com.lifengqiang.video.model.request.UserInfo;
import com.lifengqiang.video.model.result.UserDetails;
import com.lifengqiang.video.service.FollowService;
import com.lifengqiang.video.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller("api.user")
public class UserController {
    @Resource
    UserService service;

    @Resource
    FollowService followService;

    @GetMapping("/api/user/details/{id}")
    @ResponseBody
    public Result<UserDetails> getUserDetails(
            @SessionAttribute(value = "user", required = false) User user,
            @PathVariable Integer id
    ) {
        UserDetails data = service.getDetails(id);
        int currentUserId = user == null ? 0 : user.getId();
        data.setFriend(followService.isFriend(currentUserId, id));
        return Result.success(data);
    }

    @GetMapping("/user/details")
    @ResponseBody
    public Result<UserDetails> getUserDetails(@SessionAttribute("user") User user) {
        return Result.success(service.getDetails(user.getId()));
    }

    @PutMapping("/user/info")
    @ResponseBody
    public Result<UserDetails> updateUserInfo(
            HttpSession session,
            @SessionAttribute("user") User user,
            @RequestBody UserInfo info
    ) {
        User newUser = service.updateInfo(user.getId(), info);
        session.setAttribute("user", newUser);
        return Result.success(service.getDetails(newUser.getId()));
    }
}
