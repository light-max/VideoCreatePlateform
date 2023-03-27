package com.lifengqiang.video.controller;

import com.lifengqiang.video.fieldcheck.FieldCheckException;
import com.lifengqiang.video.mapper.AdminMapper;
import com.lifengqiang.video.model.entity.Admin;
import com.lifengqiang.video.util.ump.ViewModelParameter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.annotation.Resource;

@Controller
public class SetpwdController {
    @Resource
    AdminMapper mapper;

    @GetMapping("/admin/setpwd")
    @ViewModelParameter(key = "view", value = "setpwd")
    public String view(Model model) {
        return "/admin/setpwd";
    }

    @PostMapping("/admin/setpwd")
    @ViewModelParameter(key = "view", value = "setpwd")
    public String post(Model model, @SessionAttribute("admin") Admin admin,
                       String pwd0, String pwd1, String pwd2) {
        if (pwd1.equals(pwd2)) {
            try {
                Admin.builder()
                        .password(pwd0)
                        .build()
                        .check();
                if (admin.getPassword().equals(pwd0)) {
                    admin.setPassword(pwd0);
                    mapper.updateById(admin);
                    return "redirect:/admin/logout";
                } else {
                    model.addAttribute("error","旧密码输入错误");
                    return "/admin/setpwd";
                }
            } catch (FieldCheckException e) {
                model.addAttribute("error", e.getMessage());
                return "/admin/setpwd";
            }
        } else {
            model.addAttribute("error", "两次输入的密码不一致");
            return "/admin/setpwd";
        }
    }
}
