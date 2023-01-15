package com.lifengqiang.video.model.request;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Getter
public class UserAccountData {
    private int index;
    private String username;
    private String password;
    private String nickname;
    private String gender;
}
