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
import java.io.*;
import java.util.Arrays;

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

    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Users\\lifengqiang\\Desktop\\杂项\\账单.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        int sum = 0;
        while ((line = reader.readLine()) != null) {
            line = line.replace("^", "");
            String[] split = line.split(" ");
            if (split.length <= 1) {
                System.out.println("年份: " + (split.length == 1 ? split[0] : ""));
            } else {
                int s = 0;
                for (int i = 1; i < split.length; i++) {
                    s += Integer.parseInt(split[i]);
                }
                sum += s;
                System.out.println("月份: " + split[0] + " - " + s);
            }
        }
        System.out.println(sum);
    }
}
