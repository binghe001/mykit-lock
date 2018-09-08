package io.mykit.lock.redis.single.factory;

import io.mykit.lock.redis.utils.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author liuyazhuang
 * @version 1.0.0
 * @date 2018/9/8 00:12
 * @description 基础的工厂类，主要提供一些常量
 */
public class BaseFactory {

    /**
     * Redis配置文件名称
     */
    protected static final String FILE_NAME = "properties/redis-lock.properties";

    /**
     * maxIdle
     */
    protected static final String MAX_IDLE = "redis.maxIdle";

    /**
     * minIdle
     */
    protected static final String MIN_IDLE = "redis.minIdle";

    /**
     * maxTotal
     */
    protected static final String MAX_TOTAL = "redis.maxTotal";
    /**
     * Redis 主机节点
     */
    protected static final String HOST = "redis.host";
    /**
     * Redis端口
     */
    protected static final String PORT = "redis.port";


    private static volatile Properties properties;

    static {
        properties = new Properties();
        InputStream in = RedisFactory.class.getClassLoader().getResourceAsStream(FILE_NAME);
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取String类型的值
     * @param key key
     * @param defaultValue 默认的value
     * @return 获取到的value
     */
    public static String getStringValue(String key, String defaultValue){
        return properties.getProperty(key, defaultValue);
    }

    /**
     * 获取Int类型的值
     * @param key key
     * @param defaultValue 默认的value
     * @return 获取到的value
     */
    public static Integer getIntegerValue(String key, Integer defaultValue){
        String value = getStringValue(key, "");
        return StringUtils.isEmpty(value) ? defaultValue : Integer.parseInt(value);
    }
}
