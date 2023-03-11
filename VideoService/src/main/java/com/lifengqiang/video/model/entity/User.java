package com.lifengqiang.video.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lifengqiang.video.fieldcheck.annotation.*;
import com.lifengqiang.video.fieldcheck.interfaces.FieldCheckInterface;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_user")
public class User implements FieldCheckInterface<User> {
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

    @StringNonNull("昵称不能为空")
    @StringLengthMax(msg = "昵称不能超过${value}个字符", value = 12)
    private String nickname;

    @StringEnum({"male", "female"})
    private String gender;

    @DateFormat(msg = "生日格式为yyyy-MM-dd", value = "yyyy-MM-dd")
    private String birthday;

    public static DateTimeFormatter BIRTHDAY_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @StringLengthMax(msg = "个人简介不能超过${value}个字符", value = 40)
    private String des;
}
