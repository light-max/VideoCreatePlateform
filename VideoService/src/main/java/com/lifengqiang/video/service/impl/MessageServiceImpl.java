package com.lifengqiang.video.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lifengqiang.video.mapper.MessageMapper;
import com.lifengqiang.video.model.entity.Message;
import com.lifengqiang.video.model.entity.User;
import com.lifengqiang.video.model.result.LastMessage;
import com.lifengqiang.video.service.MessageService;
import com.lifengqiang.video.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {
    @Resource
    UserService userService;

    @Override
    public Message send(int send, int receive, String content) {
        Message message = Message.builder()
                .sendId(send)
                .receiveId(receive)
                .content(content)
                .build()
                .check();
        message.generateRelationKey();
        save(message);
        return message;
    }

    @Override
    public List<Message> getListAll(int user0, int user1) {
        String relationKey = Message.generateRelationKey(user0, user1);
        LambdaQueryWrapper<Message> wrapper = new QueryWrapper<Message>()
                .lambda()
                .eq(Message::getRelationKey, relationKey);
        return list(wrapper);
    }

    @Override
    public List<LastMessage> getLastMessageList(int userId) {
        List<LastMessage> messages = new ArrayList<>();
        List<Message> list = baseMapper.selectUserMessageListGroupByLastTime(userId);
        for (Message bean : list) {
            int targetId = userId == bean.getSendId() ? bean.getReceiveId() : bean.getSendId();
            User user = userService.getById(targetId);
            LastMessage message = LastMessage.builder()
                    .id(bean.getId())
                    .relationKey(bean.getRelationKey())
                    .lastTime(bean.getCreateTime())
                    .lastContent(bean.getContent())
                    .targetId(targetId)
                    .userhead("/head/" + targetId)
                    .nickname(user.getNickname())
                    .build();
            messages.add(message);
        }
        return messages;
    }
}
