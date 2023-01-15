package com.lifengqiang.video.model.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class Result<T> {
    /**
     * 状态
     */
    private Integer status = 200;
    /**
     * 信息
     */
    private String message = "操作成功";
    /**
     * 数据
     */
    private T data;

    private Result(T data) {
        this.data = data;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(data);
    }

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.status = 400;
        result.message = message;
        return result;
    }

    public static <T> Result<T> error() {
        return error(null);
    }

    /**
     * 拒绝访问
     */
    public static <T> Result<T> denied(String message) {
        Result<T> result = new Result<>();
        result.status = 403;
        result.message = message;
        return result;
    }

    public static Result<PagerData> pager(Pager pager, Object data) {
        return success(new PagerData(pager, data));
    }

    public boolean isSuccess() {
        return 200 == status;
    }
}
