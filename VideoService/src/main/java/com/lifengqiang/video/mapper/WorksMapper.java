package com.lifengqiang.video.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lifengqiang.video.model.entity.Works;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface WorksMapper extends BaseMapper<Works> {
    @Select("select id from t_works limit ${index},${count};")
    List<Integer> getIds(@Param("index") int index, @Param("count") int count);

    @Select("select id from t_works;")
    List<Integer> getAllIds();
}
