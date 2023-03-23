package com.lifengqiang.video.model.result;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RemarkResult {
    private int id;
    private int userId;
    private int worksId;
    private String content;
    private Long createTime;

    private String createDateTime;

    private String username;
    private String userNickname;
    private String userHead;

    private boolean me;
    private boolean like;
    private int likeCount;
    private int replyCount;
}
