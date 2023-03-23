package com.lifengqiang.video.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lifengqiang.video.model.entity.Like;

public interface LikeService extends IService<Like> {
    int WORKS = 0;
    int REMARK = 1;

    int getCount(int targetId, int type);

    boolean isLike(int userId, int targetId, int type);

    boolean toggle(int userId, int targetId, int type);
}
