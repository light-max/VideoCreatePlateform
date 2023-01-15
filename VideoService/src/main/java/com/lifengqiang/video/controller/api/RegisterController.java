package com.lifengqiang.video.controller.api;

import com.lifengqiang.video.constant.GlobalConstant;
import com.lifengqiang.video.model.data.Result;
import com.lifengqiang.video.model.entity.User;
import com.lifengqiang.video.service.UserService;
import com.lifengqiang.video.util.UseDefaultSuccessResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.annotation.Resource;

@Controller
public class RegisterController {
    @Resource
    UserService service;

    @PostMapping("/user/register")
    @ResponseBody
    public Result<User> register(String username, String password) {
        User data = service.addNew(username, password);
        return Result.success(data);
    }

    @PostMapping("/user/password")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void setPwd(
            @SessionAttribute("user") User user,
            String source, String password
    ) {
        GlobalConstant.sourcePasswordError.isTrue(user.getPassword().equals(source));
        User.builder().password(password)
                .build()
                .check();
        user.setPassword(password);
        service.updateById(user);
    }
}
