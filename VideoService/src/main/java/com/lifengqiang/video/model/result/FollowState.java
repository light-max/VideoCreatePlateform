package com.lifengqiang.video.model.result;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FollowState {
    private int userId;
    private int targetId;

    private boolean follow;
    private int count;
}
