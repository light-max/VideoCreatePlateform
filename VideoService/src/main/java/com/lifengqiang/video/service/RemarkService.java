package com.lifengqiang.video.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lifengqiang.video.model.entity.Remark;
import com.lifengqiang.video.model.entity.Reply;
import com.lifengqiang.video.model.result.RemarkResult;
import com.lifengqiang.video.model.result.ReplyResult;

import java.util.List;

public interface RemarkService {
    int getRemarkCount(int worksId);

    int getReplyCount(int remarkId);

    void addRemark(int userId, int worksId, String content);

    void addReply(int userId, int remarkId, String content, int replyId);

    Page<Remark> getRemarkPage(int worksId, Integer n);

    RemarkResult getRemarkResult(int currentUserId, Remark remark);

    List<RemarkResult> getRemarkResultList(int currentUserId, List<Remark> remarks);

    Remark getRemarkById(int id);

    Page<Reply> getReplyPage(int remarkId, Integer n);

    ReplyResult getReplyResult(int currentUserId, Reply reply);

    List<ReplyResult> getReplyResultList(int currentUserId, List<Reply> replys);

}
