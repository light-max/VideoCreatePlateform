package com.lifengqiang.video.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_like")
public class Like {
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 点赞的类型,0:作品,1:评论(不包括回复)
     */
    private Integer type;

    private Integer targetId;

    private Integer userId;
}
