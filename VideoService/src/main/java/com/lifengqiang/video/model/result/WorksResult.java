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

    private String username;
    private String nickname;
    private String userhead;

    private String cover;
    private String video;
    private List<String> images;

    private boolean like;
    private boolean follwuser;
    private boolean likeCount;
    private boolean remarkCount;
    private boolean collect;
}
