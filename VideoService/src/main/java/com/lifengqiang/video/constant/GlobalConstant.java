package com.lifengqiang.video.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GlobalConstant implements Assert {
    error("系统错误"),

    noAccess("没有访问权限，请重新登录"),

    usernameExists("用户名已存在"),

    loginError("登陆错误，用户名或密码不正确"),

    dataNotExists("数据不存在"),

    sourcePasswordError("原密码错误"),
    passwordsError("两次输入的密码不一致"),

    resourceNotExists("资源文件不存在"),
    resourceEmpty("没有资源文件"),

    collectError("已收藏，不能重复收藏"),
    ;

    private String message;
}
