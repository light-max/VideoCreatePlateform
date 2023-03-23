package com.lifengqiang.video.model.result;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CollectState {
    private int worksId;
    private int userId;

    private int count;
    private boolean collect;
}
