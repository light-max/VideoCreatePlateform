package com.lifengqiang.video.model.result;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class WorksResult {
    private int id;
    private int userId;
    private int type;
    private String content;
    private long createTime;

    private String date;

    private String username;
    private String nickname;
    private String userhead;

    private String cover;
    private String video;
    private List<String> images;

    private boolean follwuser;
    private boolean like;
    private int likeCount;
    private int remarkCount;
    private boolean collect;
    private int collectCount;
}
