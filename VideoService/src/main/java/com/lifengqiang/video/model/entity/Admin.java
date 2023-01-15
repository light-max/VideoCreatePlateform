package com.lifengqiang.video.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lifengqiang.video.fieldcheck.annotation.StringLengthMax;
import com.lifengqiang.video.fieldcheck.annotation.StringLengthMin;
import com.lifengqiang.video.fieldcheck.annotation.StringRegex;
import com.lifengqiang.video.fieldcheck.interfaces.FieldCheckInterface;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_admin")
public class Admin implements FieldCheckInterface<Admin> {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @StringLengthMin(msg = "用户名长度不能小于${value}位", value = 4)
    @StringLengthMax(msg = "用户名长度不能超过${value}位", value = 16)
    @StringRegex(msg = "用户名只能包含字母数字_.", value = "^[A-Za-z0-9_.]{4,16}")
    private String username;

    @StringLengthMin(msg = "密码长度不能小于${value}位", value = 4)
    @StringLengthMax(msg = "密码长度不能超过${value}位", value = 16)
    @StringRegex(msg = "密码只能包含字母数字_.", value = "^[A-Za-z0-9_.]{4,16}")
    private String password;
}
