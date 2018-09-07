package io.mykit.lock.redis.annotation;

import java.lang.annotation.*;

/**
 * @author liuyazhuang
 * @version 1.0.0
 * @date 2018/9/7 22:09
 * @description 方法级别的注解，用于注解会产生并发问题的方法
 * 在代码实现层面，注解有并发的方法和参数，通过动态代理获取注解的方法和参数，在代理中加锁，执行完被代理的方法后释放锁。
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheLock {

    /**
     * Redis锁Key的前缀
     * @return Redis锁Key的前缀
     */
    String lockedPrefix() default "";

    /**
     * 轮询锁的时间
     * @return 轮询锁的时间
     */
    long timeout() default 2000;

    /**
     * Key在Redis里存在的时间
     * @return Key在Redis里存在的时间
     */
    int expireTime() default 1000;
}
