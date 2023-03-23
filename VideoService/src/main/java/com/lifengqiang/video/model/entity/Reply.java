package com.lifengqiang.video.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.lifengqiang.video.fieldcheck.annotation.StringLengthMax;
import com.lifengqiang.video.fieldcheck.annotation.StringNonNull;
import com.lifengqiang.video.fieldcheck.interfaces.FieldCheckInterface;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_reply")
public class Reply implements FieldCheckInterface<Reply> {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer remarkId;

    private Integer userId;

    /**
     * <=0时代表没有回用户, 只是回复评论
     */
    private Integer replyId;

    @StringNonNull("回复内容不能为空")
    @StringLengthMax(msg = "回复内容不能超过${value}个字符", value = 100)
    private String content;

    @TableField(fill = FieldFill.INSERT)
    private Long createTime;
}
