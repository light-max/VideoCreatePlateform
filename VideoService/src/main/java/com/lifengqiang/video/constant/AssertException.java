package com.lifengqiang.video.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AssertException extends RuntimeException {
    private final String message;
}
