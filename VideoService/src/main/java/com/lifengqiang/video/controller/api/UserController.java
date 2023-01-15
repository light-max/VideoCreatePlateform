package com.lifengqiang.video.controller.api;

import com.lifengqiang.video.model.data.Result;
import com.lifengqiang.video.model.result.UserDetails;
import com.lifengqiang.video.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller("api.user")
public class UserController {
    @Resource
    UserService service;

    @GetMapping("/api/user/details/{id}")
    @ResponseBody
    public Result<UserDetails> getUserDetails(@PathVariable Integer id) {
        return Result.success(service.getDetails(id));
    }
}
