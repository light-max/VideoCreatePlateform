package com.lifengqiang.video.controller.api;

import com.lifengqiang.video.constant.GlobalConstant;
import com.lifengqiang.video.model.data.Result;
import com.lifengqiang.video.model.entity.User;
import com.lifengqiang.video.service.LoginService;
import com.lifengqiang.video.util.UseDefaultSuccessResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller("user.login")
public class LoginController {
    @Resource
    LoginService service;

    @PostMapping("/user/login")
    @ResponseBody
    public Result<User> login(HttpSession session, String username, String password) {
        Result<User> result = service.user(username, password);
        if (result.isSuccess()) {
            session.setAttribute("user", result.getData());
        }
        return result;
    }

    @GetMapping("/user/notloggedin")
    @ResponseBody
    public Result<Object> notLogin() {
        return Result.error(GlobalConstant.noAccess.getMessage());
    }

    @PostMapping("/user/logout")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void logout(HttpSession session) {
        session.removeAttribute("user");
    }
}
