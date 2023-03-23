package com.lifengqiang.video.model.result;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReplyResult {
    private int id;
    private int remarkId;
    private int userId;
    private int replyId;
    private String content;
    private Long createTime;

    private String createDateTime;

    private boolean me;
    private String username;
    private String userNickname;
    private String userhead;

    private boolean toReply;
    private String toUsername;
    private String toUserNickname;
}
