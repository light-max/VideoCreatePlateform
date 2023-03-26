package com.lifengqiang.video.controller.api;

import com.lifengqiang.video.model.data.Result;
import com.lifengqiang.video.model.entity.Message;
import com.lifengqiang.video.model.entity.User;
import com.lifengqiang.video.model.result.LastMessage;
import com.lifengqiang.video.service.MessageService;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class MessageController {
    @Resource
    MessageService service;

    @PostMapping("/user/message")
    @ResponseBody
    public Result<Message> send(@SessionAttribute("user") User user, Integer receive, String content) {
        return Result.success(service.send(user.getId(), receive, content));
    }

    @GetMapping("/user/message/list/all")
    @ResponseBody
    public Result<List<Message>> getAll(@SessionAttribute("user") User user, Integer receive) {
        return Result.success(service.getListAll(user.getId(), receive));
    }

    @GetMapping("/user/message/group/all")
    @ResponseBody
    public Result<List<LastMessage>> getMessageGroup(@SessionAttribute("user") User user) {
        return Result.success(service.getLastMessageList(user.getId()));
    }
}
