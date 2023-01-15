package com.lifengqiang.video.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lifengqiang.video.constant.GlobalConstant;
import com.lifengqiang.video.model.data.Pager;
import com.lifengqiang.video.model.data.Result;
import com.lifengqiang.video.model.entity.User;
import com.lifengqiang.video.model.request.UserAccountData;
import com.lifengqiang.video.service.UserService;
import com.lifengqiang.video.util.UseDefaultSuccessResponse;
import com.lifengqiang.video.util.ump.ViewModelParameter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller("admin.user")
public class UserController {
    @Resource
    UserService service;

    @GetMapping({"/admin/user", "/admin/user/list", "/admin/user/list/{n}"})
    @ViewModelParameter(key = "view", value = "user")
    public String list(
            Model model,
            @PathVariable(required = false) Integer n,
            @RequestParam(required = false) String w
    ) {
        Page<User> page = service.queryUserByKey(n, w);
        Pager pager = new Pager(page, "/admin/user/list");
        List<User> list = page.getRecords();
        if (w != null) {
            pager.setTailAppend("?w=" + w);
        }
        model.addAttribute("w", w);
        model.addAttribute("list", list);
        return "/admin/user/list";
    }

    @GetMapping("/admin/user/add")
    @ViewModelParameter(key = "view", value = "adduser")
    public String add(Model model) {
        return "/admin/user/add";
    }

    @PostMapping("/admin/user/add")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void add(@RequestBody List<UserAccountData> list) {
        service.addUserAccountList(list);
    }

    @DeleteMapping("/admin/user/delete/{id}")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void delete(@PathVariable Integer id) {
        service.deleteUserAndUserData(id);
    }

    @PutMapping("/admin/user/update/{id}")
    @ResponseBody
    public Result<User> update(@PathVariable Integer id, String nickname, String gender, String birthday, String des) {
        User user = service.getById(id);
        GlobalConstant.dataNotExists.notNull(user);
        user.setNickname(nickname);
        user.setGender(gender);
        user.setBirthday(birthday);
        user.setDes(des);
        user.check();
        service.updateById(user);
        return Result.success(user);
    }
}
