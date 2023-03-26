package com.lifengqiang.video.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lifengqiang.video.model.entity.Message;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MessageMapper extends BaseMapper<Message> {
    @Select("select *\n" +
            "from (select *\n" +
            "      from (select *\n" +
            "            from t_message\n" +
            "            where send_id = ${userId}\n" +
            "               or receive_id = ${userId}\n" +
            "            order by create_time desc) as a\n" +
            "      group by a.relation_key) as b\n" +
            "order by create_time desc;")
    List<Message> selectUserMessageListGroupByLastTime(@Param("userId") int userId);
}
