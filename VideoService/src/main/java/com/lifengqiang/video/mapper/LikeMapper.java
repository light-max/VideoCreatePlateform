package com.lifengqiang.video.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lifengqiang.video.model.entity.Like;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface LikeMapper extends BaseMapper<Like> {
    @Select("select target_id from t_like where type=0 and user_id=${v};")
    List<Integer> getWorksIdListByUserId(@Param("v") int userId);
}
