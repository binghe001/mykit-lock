package io.mykit.lock.redis.utils;

/**
 * @author liuyazhuang
 * @version 1.0.0
 * @date 2018/9/8 00:22
 * @description 字符串工具类
 */
public class StringUtils {

    public static boolean isEmpty(String str){
        return str == null || "".equals(str.trim());
    }
}
