package com.lifengqiang.video.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lifengqiang.video.model.entity.Message;
import com.lifengqiang.video.model.result.LastMessage;

import java.util.List;

public interface MessageService extends IService<Message> {
    Message send(int send, int receive, String content);

    List<Message> getListAll(int user0, int user1);

    List<LastMessage> getLastMessageList(int userId);
}
