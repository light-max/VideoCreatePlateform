package com.lifengqiang.video.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.lifengqiang.video.fieldcheck.annotation.StringLengthMax;
import com.lifengqiang.video.fieldcheck.annotation.StringNonNull;
import com.lifengqiang.video.fieldcheck.interfaces.FieldCheckInterface;
import com.lifengqiang.video.util.datetranslate.DateParameter;
import com.lifengqiang.video.util.datetranslate.DateTranslate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_remark")
public class Remark implements FieldCheckInterface<Remark>, DateTranslate {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer worksId;

    private Integer userId;

    @StringNonNull("评论内容不能为空")
    @StringLengthMax(msg = "评论内容不能超过${value}个字符", value = 100)
    private String content;

    @DateParameter("yyyy-MM-dd HH:mm")
    @TableField(fill = FieldFill.INSERT)
    private Long createTime;
}
