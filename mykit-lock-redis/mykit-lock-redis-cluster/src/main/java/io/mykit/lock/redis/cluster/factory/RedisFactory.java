package io.mykit.lock.redis.cluster.factory;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * @author liuyazhuang
 * @version 1.0.0
 * @date 2018/9/8 12:24
 * @description Redis工厂类
 */
public class RedisFactory extends BaseFactory{

    /**
     * 配置JedisCluster句柄
     * @return JedisCluster句柄
     */
    public static JedisCluster getJedisCluster(){
        JedisCluster jedisCluster = new JedisCluster(getHostAndPorts(),getIntegerValue(CLUSTER_TIMEOUT), getIntegerValue(CLUSTER_TIMEOUT), getIntegerValue(CLUSTER_MAXATTEMPTS), getPoolConfig());
        //JedisCluster jedisCluster = new JedisCluster(getHostAndPorts());
        return jedisCluster;
    }

    /**
     * 配置Redis集群缓存
     * @return Redis集群缓存池配置
     */
    private static JedisPoolConfig getPoolConfig(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(getIntegerValue(CLUSTER_MAX_TOTAL));
        jedisPoolConfig.setMaxIdle(getIntegerValue(CLUSTER_MAX_IDLE));
        jedisPoolConfig.setMinIdle(getIntegerValue(CLUSTER_MIN_IDLE));
        jedisPoolConfig.setBlockWhenExhausted(getBooleanValue(CLUSTER_BLOCKWHENEXHAUSTED));
        jedisPoolConfig.setMaxWaitMillis(getIntegerValue(CLUSTER_MAXWAITMILLIS));
        jedisPoolConfig.setTestOnBorrow(getBooleanValue(CLUSTER_TESTONBORROW));
        jedisPoolConfig.setTestOnReturn(getBooleanValue(CLUSTER_TESTONRETURN));
        jedisPoolConfig.setTestWhileIdle(getBooleanValue(CLUSTER_TESTWHILEIDLE));
        jedisPoolConfig.setMinEvictableIdleTimeMillis(getIntegerValue(CLUSTER_MINEVICTABLEIDLETIMEMILLIS));
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(getIntegerValue(CLUSTER_TIMEBETWEENEVICTIONRUNSMILLIS));
        jedisPoolConfig.setNumTestsPerEvictionRun(getIntegerValue(CLUSTER_NUMTESTSPEREVICTIONRUN));
        return jedisPoolConfig;
    }


    /**
     * 配置主机和节点信息
     * @return 配置主机和节点信息集合
     */
   private static Set<HostAndPort> getHostAndPorts(){
        Set<HostAndPort> hostAndPorts = new HashSet<HostAndPort>();
        hostAndPorts.add(new HostAndPort(getStringValue(CLUSTER_NODE_ONE), getIntegerValue(CLUSTER_NODE_ONE_PORT)));
        hostAndPorts.add(new HostAndPort(getStringValue(CLUSTER_NODE_TWO), getIntegerValue(CLUSTER_NODE_TWO_PORT)));
        hostAndPorts.add(new HostAndPort(getStringValue(CLUSTER_NODE_THREE), getIntegerValue(CLUSTER_NODE_THREE_PORT)));
        hostAndPorts.add(new HostAndPort(getStringValue(CLUSTER_NODE_FOUR), getIntegerValue(CLUSTER_NODE_FOUR_PORT)));
        hostAndPorts.add(new HostAndPort(getStringValue(CLUSTER_NODE_FIVE), getIntegerValue(CLUSTER_NODE_FIVE_PORT)));
        hostAndPorts.add(new HostAndPort(getStringValue(CLUSTER_NODE_SIX), getIntegerValue(CLUSTER_NODE_SIX_PORT)));
        hostAndPorts.add(new HostAndPort(getStringValue(CLUSTER_NODE_SEVEN), getIntegerValue(CLUSTER_NODE_SEVEN_PORT)));
        return hostAndPorts;
   }
}
