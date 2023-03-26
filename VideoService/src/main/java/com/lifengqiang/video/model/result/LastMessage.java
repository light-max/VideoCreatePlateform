package com.lifengqiang.video.model.result;

import com.lifengqiang.video.fieldcheck.annotation.StringLengthMax;
import lombok.Builder;
import lombok.Data;

/**
 * 用户和某个对象的最后一条消息
 */
@Data
@Builder
public class LastMessage {
    private int id;
    private String relationKey;
    private int targetId;
    private String lastContent;
    private long lastTime;
    private String nickname;
    private String userhead;
}
