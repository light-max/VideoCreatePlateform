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
@TableName("t_message")
public class Message implements FieldCheckInterface<Message> {
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 关系键, 计算方式为"${min(a,b)}_${max(a,b)}", ab为用户id
     */
    private String relationKey;

    private Integer sendId;

    private Integer receiveId;

    @StringNonNull("消息不能为空")
    @StringLengthMax(msg = "消息长度不能超过${value}", value = 200)
    private String content;

    @TableField(fill = FieldFill.INSERT)
    private Long createTime;

    public void generateRelationKey() {
        relationKey = Math.min(sendId, receiveId) + "_" + Math.max(sendId, receiveId);
    }

    public static String generateRelationKey(int user0, int user1) {
        return Math.min(user0, user1) + "_" + Math.max(user0, user1);
    }
}
