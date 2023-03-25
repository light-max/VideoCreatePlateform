package com.lifengqiang.video.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lifengqiang.video.model.entity.Collect;

import java.util.List;

public interface CollectService extends IService<Collect> {
    int getCount(int worksId);

    boolean isCollect(int userId, int worksId);

    boolean toggle(int userId, int worksId);

    List<Integer> getCollectWorksIdList(int userId);
}
