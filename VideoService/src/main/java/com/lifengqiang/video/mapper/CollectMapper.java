package com.lifengqiang.video.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lifengqiang.video.model.entity.Collect;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CollectMapper extends BaseMapper<Collect> {
    @Select("select works_id from t_collect where user_id=${v};")
    List<Integer> getWorksIdListByUserId(@Param("v") int userId);
}
