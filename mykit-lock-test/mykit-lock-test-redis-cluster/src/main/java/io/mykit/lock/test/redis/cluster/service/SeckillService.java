package io.mykit.lock.test.redis.cluster.service;

import io.mykit.lock.redis.annotation.CacheLock;
import io.mykit.lock.redis.annotation.LockedObject;

/**
 * @author liuyazhuang
 * @version 1.0.0
 * @date 2018/9/7 23:36
 * @description 商品秒杀Service
 */
public interface SeckillService {

    @CacheLock(lockedPrefix="TEST_PREFIX")
    public void secKill(String arg1, @LockedObject Long arg2);
}
