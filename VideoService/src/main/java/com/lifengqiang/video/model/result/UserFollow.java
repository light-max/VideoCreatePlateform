package com.lifengqiang.video.model.result;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserFollow {
    /**
     * 0:关注
     * 1:粉丝
     * 2:朋友
     */
    private int type;
    private int userId;
    private String username;
    private String nickname;
    private String head;

    private boolean friend;
    private boolean follow;
    private boolean follower;
}
