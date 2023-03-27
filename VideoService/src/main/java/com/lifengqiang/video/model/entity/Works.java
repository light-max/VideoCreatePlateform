package com.lifengqiang.video.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.lifengqiang.video.fieldcheck.annotation.DateFormat;
import com.lifengqiang.video.fieldcheck.annotation.NumberEnum;
import com.lifengqiang.video.fieldcheck.annotation.StringLengthMax;
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
@TableName("t_works")
public class Works implements FieldCheckInterface<Works>, DateTranslate {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    /**
     * 0图片 1视频
     */
    @NumberEnum({0, 1})
    private Integer type;

    @StringLengthMax(msg = "内容不能超过${value}个字符串", value = 200)
    private String content;

    @DateParameter("yyyy-MM-dd HH:mm")
    @TableField(fill = FieldFill.INSERT)
    private Long createTime;
}
