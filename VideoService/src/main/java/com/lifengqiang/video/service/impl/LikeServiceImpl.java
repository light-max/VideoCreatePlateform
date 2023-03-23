package com.lifengqiang.video.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lifengqiang.video.mapper.LikeMapper;
import com.lifengqiang.video.model.entity.Like;
import com.lifengqiang.video.service.LikeService;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl extends ServiceImpl<LikeMapper, Like> implements LikeService {
    @Override
    public int getCount(int targetId, int type) {
        return count(new QueryWrapper<Like>()
                .lambda()
                .eq(Like::getTargetId, targetId)
                .eq(Like::getType, type));
    }

    @Override
    public boolean isLike(int userId, int targetId, int type) {
        LambdaQueryWrapper<Like> wrapper = new QueryWrapper<Like>()
                .lambda()
                .eq(Like::getType, type)
                .eq(Like::getTargetId, targetId)
                .eq(Like::getUserId, userId);
        return count(wrapper) > 0;
    }

    @Override
    public boolean toggle(int userId, int targetId, int type) {
        LambdaQueryWrapper<Like> wrapper = new QueryWrapper<Like>()
                .lambda()
                .eq(Like::getType, type)
                .eq(Like::getTargetId, targetId)
                .eq(Like::getUserId, userId);
        Like like = getOne(wrapper);
        if (like == null) {
            save(Like.builder().type(type)
                    .targetId(targetId)
                    .userId(userId)
                    .build());
            return true;
        } else {
            removeById(like.getId());
            return false;
        }
    }
}
