package com.lifengqiang.video.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lifengqiang.video.model.entity.Follow;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface FollowMapper extends BaseMapper<Follow> {
    @Select("select a.target_id\n" +
            "from t_follow a\n" +
            "where user_id = ${value}\n" +
            "  and exists(\n" +
            "        select 1\n" +
            "        from t_follow b\n" +
            "        where b.user_id = a.target_id\n" +
            "          and b.target_id = a.user_id\n" +
            ")")
    List<Integer> selectFriendIdsByUserId(@Param("value") int userId);

    @Select("select count(1)\n" +
            "from t_follow a\n" +
            "where user_id = ${value}\n" +
            "  and exists(\n" +
            "        select 1\n" +
            "        from t_follow b\n" +
            "        where b.user_id = a.target_id\n" +
            "          and b.target_id = a.user_id\n" +
            ")")
    int selectFriendCountByUserId(@Param("value") int userId);
}
