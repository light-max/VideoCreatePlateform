package com.lifengqiang.video.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lifengqiang.video.mapper.RemarkMapper;
import com.lifengqiang.video.mapper.ReplyMapper;
import com.lifengqiang.video.mapper.UserMapper;
import com.lifengqiang.video.model.entity.Remark;
import com.lifengqiang.video.model.entity.Reply;
import com.lifengqiang.video.model.entity.User;
import com.lifengqiang.video.model.result.RemarkResult;
import com.lifengqiang.video.model.result.ReplyResult;
import com.lifengqiang.video.service.LikeService;
import com.lifengqiang.video.service.RemarkService;
import com.lifengqiang.video.util.datetranslate.DateFormatUtil;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class RemarkServiceImpl implements RemarkService {
    @Resource
    RemarkMapper remarkMapper;

    @Resource
    ReplyMapper replyMapper;

    @Resource
    UserMapper userMapper;

    @Resource
    LikeService likeService;

    @Override
    public int getRemarkCount(int worksId) {
        return remarkMapper.selectCount(new QueryWrapper<Remark>()
                .lambda()
                .eq(Remark::getWorksId, worksId));
    }

    @Override
    public int getReplyCount(int remarkId) {
        return replyMapper.selectCount(new QueryWrapper<Reply>()
                .lambda()
                .eq(Reply::getRemarkId, remarkId));
    }

    @Override
    public void addRemark(int userId, int worksId, String content) {
        Remark remark = Remark.builder()
                .userId(userId)
                .worksId(worksId)
                .content(content)
                .build()
                .check();
        remarkMapper.insert(remark);
    }

    @Override
    public void addReply(int userId, int remarkId, String content, int replyId) {
        Reply reply = Reply.builder()
                .userId(userId)
                .remarkId(remarkId)
                .content(content)
                .replyId(replyId)
                .build()
                .check();
        replyMapper.insert(reply);
    }

    @Override
    public Page<Remark> getRemarkPage(int worksId, Integer n) {
        Page<Remark> page = new Page<>(n == null ? 1 : n, 1000);
        LambdaQueryWrapper<Remark> wrapper = new QueryWrapper<Remark>()
                .lambda()
                .eq(Remark::getWorksId, worksId)
                .orderByDesc(Remark::getCreateTime);
        return remarkMapper.selectPage(page, wrapper);
    }

    @Override
    public RemarkResult getRemarkResult(int currentUserId, Remark remark) {
        User user = userMapper.selectById(remark.getUserId());
        return RemarkResult.builder()
                .id(remark.getId())
                .worksId(remark.getWorksId())
                .userId(remark.getUserId())
                .content(remark.getContent())
                .createTime(remark.getCreateTime())
                .createDateTime(DateFormatUtil.format(remark.getCreateTime()))
                .userHead("/head/" + user.getId())
                .username(user.getUsername())
                .userNickname(user.getNickname())
                .me(remark.getUserId() == currentUserId)
                .like(likeService.isLike(currentUserId, remark.getId(), LikeService.REMARK))
                .likeCount(likeService.getCount(remark.getId(), LikeService.REMARK))
                .replyCount(getReplyCount(remark.getId()))
                .build();
    }

    @Override
    public List<RemarkResult> getRemarkResultList(int currentUserId, List<Remark> remarks) {
        List<RemarkResult> results = new ArrayList<>();
        for (Remark remark : remarks) {
            results.add(getRemarkResult(currentUserId, remark));
        }
        return results;
    }

    @Override
    public Remark getRemarkById(int id) {
        return remarkMapper.selectById(id);
    }

    @Override
    public Page<Reply> getReplyPage(int remarkId, Integer n) {
        Page<Reply> page = new Page<>(n == null ? 1 : n, 1000);
        LambdaQueryWrapper<Reply> wrapper = new QueryWrapper<Reply>()
                .lambda()
                .eq(Reply::getRemarkId, remarkId)
                .orderByDesc(Reply::getCreateTime);
        return replyMapper.selectPage(page, wrapper);
    }

    @Override
    public ReplyResult getReplyResult(int currentUserId, Reply reply) {
        User user = userMapper.selectById(reply.getUserId());
        User toUser = userMapper.selectById(reply.getReplyId());
        if (toUser == null) {
            toUser = User.builder()
                    .id(0)
                    .username("")
                    .nickname("")
                    .build();
        }
        return ReplyResult.builder()
                .id(reply.getId())
                .remarkId(reply.getRemarkId())
                .userId(reply.getUserId())
                .replyId(reply.getReplyId())
                .content(reply.getContent())
                .createTime(reply.getCreateTime())
                .createDateTime(DateFormatUtil.format(reply.getCreateTime()))
                .me(currentUserId == user.getId())
                .username(user.getUsername())
                .userNickname(user.getNickname())
                .userhead("/head/" + user.getId())
                .toReply(toUser.getId() != 0)
                .toUsername(toUser.getUsername())
                .toUserNickname(toUser.getNickname())
                .build();
    }

    @Override
    public List<ReplyResult> getReplyResultList(int currentUserId, List<Reply> replys) {
        List<ReplyResult> results = new ArrayList<>();
        for (Reply reply : replys) {
            results.add(getReplyResult(currentUserId, reply));
        }
        return results;
    }
}
