package com.lifengqiang.video.model.result;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchValue {
    private int id;
    private String primary;
    private String cover;
}
