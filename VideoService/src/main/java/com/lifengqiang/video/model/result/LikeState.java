package com.lifengqiang.video.model.result;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LikeState {
    private int type;
    private int targetId;
    private int userId;

    private int count;
    private boolean like;
}
