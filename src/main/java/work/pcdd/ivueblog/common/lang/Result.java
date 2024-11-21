package work.pcdd.ivueblog.common.lang;

import lombok.Data;

import java.io.Serializable;

/**
 * @author pcdd
 */
@Data
public class Result implements Serializable {
    private Integer code;
    private String msg;
    private Object data;

    public static Result success() {
        return Result.success(null);
    }

    public static Result success(Object data) {
        Result result = new Result();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMsg(ResultCode.SUCCESS.getMessage());
        result.setData(data);
        return result;
    }

    /**
     * @param msg 系统抛出的异常信息
     */
    public static Result failure(String msg) {
        return Result.failure(400, msg);
    }

    /**
     * @param msg 系统抛出的异常信息 自定义状态码
     */
    public static Result failure(int code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    /**
     * @param resultCode 自定义异常枚举
     */
    public static Result failure(ResultCode resultCode) {
        return failure(resultCode, null);
    }

    /**
     * @param resultCode 自定义异常枚举
     * @param data       异常数据
     */
    public static Result failure(ResultCode resultCode, Object data) {
        Result result = new Result();
        result.setCode(resultCode.getCode());
        result.setMsg(resultCode.getMessage());
        result.setData(data);
        return result;
    }
}
