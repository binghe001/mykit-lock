package io.mykit.lock.redis.annotation;

import java.lang.annotation.*;

/**
 * @author liuyazhuang
 * @version 1.0.0
 * @date 2018/9/7 22:45
 * @description 锁对象
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface LockedObject {

}
