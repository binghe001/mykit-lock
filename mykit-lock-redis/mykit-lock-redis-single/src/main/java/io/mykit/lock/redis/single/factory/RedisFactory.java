package io.mykit.lock.redis.single.factory;

import io.mykit.lock.redis.single.client.RedisClient;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author liuyazhuang
 * @version 1.0.0
 * @date 2018/9/7 23:01
 * @description Redis工厂类
 */
public class RedisFactory extends BaseFactory {



    /**
     * 设置Redis缓存配置
     * @return Redis缓存配置
     * @throws IOException
     */
    public static JedisPoolConfig getPoolConfig() throws IOException {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(getIntegerValue(MAX_IDLE, 100));
        config.setMinIdle(getIntegerValue(MIN_IDLE, 1));
        config.setMaxTotal(getIntegerValue(MAX_TOTAL, 1000));
        return config;
    }

    /**
     * 获取默认的缓存配置
     * @return
     */
    public static RedisClient getDefaultClient(){
        JedisPool pool = new JedisPool(getStringValue(HOST, "127.0.0.1"), getIntegerValue(PORT, 6379));
        RedisClient client = new RedisClient(pool);
        return client;
    }
}
