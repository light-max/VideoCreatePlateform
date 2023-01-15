package com.lifengqiang.video.data;

public class Res<T> {
    private int status;
    private String message;
    T data;

    public T getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return status == 200;
    }

    public boolean isDenied() {
        return status == 403;
    }
}
