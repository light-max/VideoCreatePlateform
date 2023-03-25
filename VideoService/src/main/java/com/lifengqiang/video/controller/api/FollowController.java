package com.lifengqiang.video.controller.api;

import com.lifengqiang.video.model.data.Result;
import com.lifengqiang.video.model.entity.User;
import com.lifengqiang.video.model.result.*;
import com.lifengqiang.video.service.FollowService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class FollowController {
    @Resource
    FollowService service;

    @GetMapping("/user/follow/{targetId}")
    @ResponseBody
    public Result<FollowState> follow(
            @SessionAttribute("user") User user,
            @PathVariable Integer targetId
    ) {
        boolean follow = service.isFollow(user.getId(), targetId);
        int count = service.getFollowCount(targetId);
        FollowState state = FollowState.builder()
                .userId(user.getId())
                .targetId(targetId)
                .follow(follow)
                .count(count)
                .build();
        return Result.success(state);
    }

    @PostMapping("/user/follow/{targetId}")
    @ResponseBody
    public Result<FollowState> follower(
            @SessionAttribute("user") User user,
            @PathVariable Integer targetId
    ) {
        boolean follow = service.follow(user.getId(), targetId);
        int count = service.getFollowCount(targetId);
        FollowState state = FollowState.builder()
                .userId(user.getId())
                .targetId(targetId)
                .count(count)
                .follow(follow)
                .build();
        return Result.success(state);
    }

    @GetMapping("/user/friends")
    @ResponseBody
    public Result<List<UserFollow>> getFriendList(@SessionAttribute("user") User user) {
        return Result.success(service.getFriends(user.getId()));
    }

    @GetMapping("/user/follow/list")
    @ResponseBody
    public Result<List<UserFollow>> getFollowList(@SessionAttribute("user") User user) {
        return Result.success(service.getFollows(user.getId()));
    }

    @GetMapping("/user/follower/list")
    @ResponseBody
    public Result<List<UserFollow>> getFollowerList(@SessionAttribute("user") User user) {
        return Result.success(service.getFollowers(user.getId()));
    }

    @GetMapping("/user/friend/{targetId}")
    @ResponseBody
    public Result<ISFriend> friend(@SessionAttribute("user") User user, @PathVariable Integer targetId) {
        boolean friend = service.isFriend(user.getId(), targetId);
        ISFriend f;
        if (friend) {
            f = ISFriend.builder()
                    .userId(user.getId())
                    .targetId(targetId)
                    .friend(true)
                    .follow(true)
                    .follower(true)
                    .build();
        } else {
            boolean follow = service.isFollow(user.getId(), targetId);
            boolean follower = service.isFollow(targetId, user.getId());
            f = ISFriend.builder()
                    .userId(user.getId())
                    .targetId(targetId)
                    .friend(false)
                    .follow(follow)
                    .follower(follower)
                    .build();
        }
        return Result.success(f);
    }
}
