package io.mykit.lock.redis.cluster.factory;

import io.mykit.lock.redis.utils.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author liuyazhuang
 * @version 1.0.0
 * @date 2018/9/8 12:13
 * @description 基础工厂类
 */
public class BaseFactory {

    protected static final String FILE_NAME = "properties/redis-lock.properties";
    //Redis集群配置
    protected static final String CLUSTER_PASSWORD = "redis.cluster.password";
    protected static final String CLUSTER_MAX_TOTAL = "redis.cluster.max.total";
    protected static final String CLUSTER_MAX_IDLE = "redis.cluster.max.idle";
    protected static final String CLUSTER_MIN_IDLE = "redis.cluster.min.idle";
    protected static final String CLUSTER_TIMEOUT = "redis.cluster.timeout";
    protected static final String CLUSTER_MAXATTEMPTS = "redis.cluster.maxAttempts";
    protected static final String CLUSTER_REDISDEFAULTEXPIRATION = "redis.cluster.redisDefaultExpiration";
    protected static final String CLUSTER_USEPREFIX = "redis.cluster.usePrefix";
    protected static final String CLUSTER_BLOCKWHENEXHAUSTED = "redis.cluster.blockWhenExhausted";
    protected static final String CLUSTER_MAXWAITMILLIS = "redis.cluster.maxWaitMillis";
    protected static final String CLUSTER_TESTONBORROW = "redis.cluster.testOnBorrow";
    protected static final String CLUSTER_TESTONRETURN = "redis.cluster.testOnReturn";
    protected static final String CLUSTER_TESTWHILEIDLE = "redis.cluster.testWhileIdle";
    protected static final String CLUSTER_MINEVICTABLEIDLETIMEMILLIS = "redis.cluster.minEvictableIdleTimeMillis";
    protected static final String CLUSTER_TIMEBETWEENEVICTIONRUNSMILLIS = "redis.cluster.timeBetweenEvictionRunsMillis";
    protected static final String CLUSTER_NUMTESTSPEREVICTIONRUN = "redis.cluster.numTestsPerEvictionRun";
    protected static final String CLUSTER_DEFAULTEXPIRATIONKEY = "redis.cluster.defaultExpirationKey";
    protected static final String CLUSTER_EXPIRATIONSECONDTIME = "redis.cluster.expirationSecondTime";
    protected static final String CLUSTER_PRELOADSECONDTIME = "redis.cluster.preloadSecondTime";
    protected static final String INDEX_ZERO = "redis.cluster.index.zero";
    protected static final String INDEX_ONE = "redis.cluster.index.one";
    protected static final String INDEX_TWO = "redis.cluster.index.two";
    protected static final String INDEX_THREE = "redis.cluster.index.three";

    //集群节点信息
    protected static final String CLUSTER_NODE_ONE = "redis.cluster.node.one";
    protected static final String CLUSTER_NODE_ONE_PORT = "redis.cluster.node.one.port";

    protected static final String CLUSTER_NODE_TWO = "redis.cluster.node.two";
    protected static final String CLUSTER_NODE_TWO_PORT = "redis.cluster.node.two.port";

    protected static final String CLUSTER_NODE_THREE = "redis.cluster.node.three";
    protected static final String CLUSTER_NODE_THREE_PORT = "redis.cluster.node.three.port";

    protected static final String CLUSTER_NODE_FOUR = "redis.cluster.node.four";
    protected static final String CLUSTER_NODE_FOUR_PORT = "redis.cluster.node.four.port";

    protected static final String CLUSTER_NODE_FIVE = "redis.cluster.node.five";
    protected static final String CLUSTER_NODE_FIVE_PORT = "redis.cluster.node.five.port";

    protected static final String CLUSTER_NODE_SIX = "redis.cluster.node.six";
    protected static final String CLUSTER_NODE_SIX_PORT = "redis.cluster.node.six.port";

    protected static final String CLUSTER_NODE_SEVEN = "redis.cluster.node.seven";
    protected static final String CLUSTER_NODE_SEVEN_PORT = "redis.cluster.node.seven.port";


    private static volatile Properties properties;

    static {
        properties = new Properties();
        InputStream in = BaseFactory.class.getClassLoader().getResourceAsStream(FILE_NAME);
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
     * 获取String类型的值
     * @param key key
     * @return 获取到的value
     */
    public static String getStringValue(String key){
        return getStringValue(key, "");
    }

    /**
     * 获取Int类型的值
     * @param key key
     * @param defaultValue 默认的value
     * @return 获取到的value
     */
    public static Integer getIntegerValue(String key, Integer defaultValue){
        String value = getStringValue(key);
        return StringUtils.isEmpty(value) ? defaultValue : Integer.parseInt(value);
    }

    /**
     * 获取Int类型的值
     * @param key key
     * @return 获取到的value
     */
    public static Integer getIntegerValue(String key){
        return getIntegerValue(key, 1);
    }

    /**
     * 获取Boolean类型的值
     * @param key key
     * @return 获取到的value
     */
    public static Boolean getBooleanValue(String key){
        String value = getStringValue(key);
        return StringUtils.isEmpty(value) ? false : Boolean.parseBoolean(value);
    }
}
