package io.mykit.lock.redis.utils;

import com.alibaba.fastjson.JSONObject;

/**
 * @author liuyazhuang
 * @version 1.0.0
 * @date 2018/9/7 22:51
 * @description Json工具类
 */
public class JsonUtils {

    public static String bean2Json(Object obj){
        return JSONObject.toJSONString(obj);
    }

    public static <T> T json2Bean(String json, Class<T> clazz){
        return JSONObject.parseObject(json, clazz);
    }
}
