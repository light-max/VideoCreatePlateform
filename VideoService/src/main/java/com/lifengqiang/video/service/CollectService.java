package com.lifengqiang.video.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lifengqiang.video.model.entity.Collect;

public interface CollectService extends IService<Collect> {
    int getCount(int worksId);

    boolean isCollect(int userId, int worksId);

    boolean toggle(int userId, int worksId);
}
