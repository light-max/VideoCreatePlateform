package com.lifengqiang.video.model.result;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Data
public class UserDetails {
    private int id;
    private String username;
    private String nickname;
    private String gender;
    private String birthday;
    private String des;

    private int age;
    private boolean friend;
    private String head;
    private int videoCount;
    private int followCount;
    private int followerCount;
    private int friendCount;
}
