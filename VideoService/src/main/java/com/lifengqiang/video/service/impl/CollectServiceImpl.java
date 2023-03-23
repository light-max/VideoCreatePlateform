package com.lifengqiang.video.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lifengqiang.video.mapper.CollectMapper;
import com.lifengqiang.video.model.entity.Collect;
import com.lifengqiang.video.service.CollectService;
import org.springframework.stereotype.Service;

@Service
public class CollectServiceImpl extends ServiceImpl<CollectMapper, Collect> implements CollectService {
    @Override
    public int getCount(int worksId) {
         return count(new QueryWrapper<Collect>()
                .lambda()
                .eq(Collect::getWorksId, worksId));
    }

    @Override
    public boolean isCollect(int userId, int worksId) {
        LambdaQueryWrapper<Collect> wrapper = new QueryWrapper<Collect>()
                .lambda()
                .eq(Collect::getWorksId, worksId)
                .eq(Collect::getUserId, userId);
        return count(wrapper) > 0;
    }

    @Override
    public boolean toggle(int userId, int worksId) {
        LambdaQueryWrapper<Collect> wrapper = new QueryWrapper<Collect>()
                .lambda()
                .eq(Collect::getWorksId, worksId)
                .eq(Collect::getUserId, userId);
        Collect collect = getOne(wrapper);
        if (collect == null) {
            save(Collect.builder().worksId(worksId)
                    .userId(userId)
                    .build());
            return true;
        } else {
            removeById(collect.getId());
            return false;
        }
    }
}
