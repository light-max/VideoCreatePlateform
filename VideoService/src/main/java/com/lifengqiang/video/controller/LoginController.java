package com.lifengqiang.video.controller;

import com.lifengqiang.video.constant.GlobalConstant;
import com.lifengqiang.video.model.data.Result;
import com.lifengqiang.video.model.entity.Admin;
import com.lifengqiang.video.service.LoginService;
import com.lifengqiang.video.util.ump.ViewModelParameter;
import com.lifengqiang.video.util.ump.ViewModelParameters;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller("admin.login")
public class LoginController {
    @Resource
    LoginService service;

    @GetMapping("/admin/login")
    public String login() {
        return "/login";
    }

    @PostMapping("/admin/login")
    public String login(HttpSession session, Model model, String username, String password) {
        Result<Admin> result = service.admin(username, password);
        if (result.isSuccess()) {
            session.setAttribute("admin", result.getData());
            return "redirect:/admin";
        } else {
            model.addAttribute("username", username);
            model.addAttribute("password", password);
            model.addAttribute("error", result.getMessage());
            return "/login";
        }
    }

    @GetMapping(value = "/admin/notloggedin", produces = "text/html")
    @ViewModelParameters({
            @ViewModelParameter(key = "username", value = "admin"),
            @ViewModelParameter(key = "password", value = "1234")
    })
    public String notLogin(Model model) {
        model.addAttribute("error", GlobalConstant.noAccess.getMessage());
        return "/login";
    }

    @GetMapping("/admin/notloggedin")
    @ResponseBody
    public Result<Object> notLogin() {
        return Result.error(GlobalConstant.noAccess.getMessage());
    }

    @GetMapping("/admin/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("admin");
        return "redirect:/admin/login";
    }
}
