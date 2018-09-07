package io.mykit.lock.redis.exception;

/**
 * @author liuyazhuang
 * @version 1.0.0
 * @date 2018/9/7 22:40
 * @description 自定义异常
 */
public class CacheLockException extends Throwable{
    private String msg;

    public CacheLockException() {
    }

    public CacheLockException(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
