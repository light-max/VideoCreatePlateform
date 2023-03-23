package com.lifengqiang.video.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lifengqiang.video.mapper.FollowMapper;
import com.lifengqiang.video.model.entity.Collect;
import com.lifengqiang.video.model.entity.Follow;
import com.lifengqiang.video.model.entity.User;
import com.lifengqiang.video.model.result.UserDetails;
import com.lifengqiang.video.model.result.UserFollow;
import com.lifengqiang.video.service.FollowService;
import com.lifengqiang.video.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements FollowService {
    @Resource
    @Lazy
    UserService userService;

    @Override
    public int getFollowCount(int userId) {
        return count(new QueryWrapper<Follow>()
                .lambda()
                .eq(Follow::getUserId, userId));
    }

    @Override
    public int getFollowerCount(int targetId) {
        return count(new QueryWrapper<Follow>()
                .lambda()
                .eq(Follow::getTargetId, targetId));
    }

    @Override
    public int getFriendCount(int userId) {
        return baseMapper.selectFriendCountByUserId(userId);
    }

    @Override
    public boolean isFollow(int userId, int targetId) {
        LambdaQueryWrapper<Follow> wrapper = new QueryWrapper<Follow>()
                .lambda()
                .eq(Follow::getUserId, userId)
                .eq(Follow::getTargetId, targetId);
        return count(wrapper) > 0;
    }

    @Override
    public boolean follow(int userId, int targetId) {
        LambdaQueryWrapper<Follow> wrapper = new QueryWrapper<Follow>()
                .lambda()
                .eq(Follow::getUserId, userId)
                .eq(Follow::getTargetId, targetId);
        Follow follow = getOne(wrapper);
        if (follow == null) {
            save(Follow.builder().userId(userId)
                    .targetId(targetId)
                    .build());
            return true;
        } else {
            removeById(follow.getId());
            return false;
        }
    }

    @Override
    public boolean isFriend(int user0, int user1) {
        return isFollow(user0, user1) && isFollow(user1, user0);
    }

    @Override
    public List<UserFollow> getFriends(int userId) {
        List<Integer> ids = baseMapper.selectFriendIdsByUserId(userId);
        return getUserFollow(2, userId, ids);
    }

    @Override
    public List<UserFollow> getFollows(int userId) {
        LambdaQueryWrapper<Follow> wrapper = new QueryWrapper<Follow>()
                .lambda()
                .eq(Follow::getUserId, userId);
        List<Follow> list = list(wrapper);
        if (list.isEmpty()) {
            return new ArrayList<>();
        } else {
            List<Integer> ids = new ArrayList<>();
            for (Follow follow : list) {
                ids.add(follow.getTargetId());
            }
            return getUserFollow(0, userId, ids);
        }
    }

    @Override
    public List<UserFollow> getFollowers(int userId) {
        LambdaQueryWrapper<Follow> wrapper = new QueryWrapper<Follow>()
                .lambda()
                .eq(Follow::getTargetId, userId);
        List<Follow> list = list(wrapper);
        if (list.isEmpty()) {
            return new ArrayList<>();
        } else {
            List<Integer> ids = new ArrayList<>();
            for (Follow follow : list) {
                ids.add(follow.getUserId());
            }
            return getUserFollow(1, userId, ids);
        }
    }

    private List<UserFollow> getUserFollow(int type, int userId, List<Integer> ids) {
        List<User> users = userService.listByIds(ids);
        List<UserFollow> follows = new ArrayList<>();
        for (User user : users) {
            UserFollow follow = UserFollow.builder()
                    .userId(user.getId())
                    .username(user.getUsername())
                    .nickname(user.getNickname())
                    .head("/head/" + user.getId())
                    .type(type)
                    .follow(isFollow(userId, user.getId()))
                    .follower(isFollow(user.getId(), userId))
                    .friend(isFriend(userId, user.getId()))
                    .build();
            follows.add(follow);
        }
        return follows;
    }
}
