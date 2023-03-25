package com.lifengqiang.video.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lifengqiang.video.model.entity.Follow;
import com.lifengqiang.video.model.entity.Works;
import com.lifengqiang.video.model.result.UserDetails;
import com.lifengqiang.video.model.result.UserFollow;

import java.util.List;

public interface FollowService extends IService<Follow> {
    int getFollowCount(int userId);

    int getFollowerCount(int targetId);

    int getFriendCount(int userId);

    boolean isFollow(int userId, int targetId);

    boolean follow(int userId, int targetId);

    boolean isFriend(int user0, int user1);

    List<UserFollow> getFriends(int userId);

    List<UserFollow> getFollows(int userId);

    List<UserFollow> getFollowers(int userId);

    List<Integer> getFollowWorksIdList(int userId);

    List<Works> getFriendWorksIdList(int userId);
}
