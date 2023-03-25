package com.lifengqiang.video.model.result;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ISFriend {
    private int userId;
    private int targetId;
    private boolean friend;
    private boolean follow;
    private boolean follower;
}
