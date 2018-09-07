package io.mykit.lock.redis.annotation;

import java.lang.annotation.*;

/**
 * @author liuyazhuang
 * @version 1.0.0
 * @date 2018/9/7 22:14
 * @description 参数级别的注解，用于注解基本类型的参数
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface LockedComplexObject {

    /**
     * 含有成员变量的复杂对象中需要加锁的成员变量，如一个商品对象的商品id等
     * @return 含有成员变量的复杂对象中需要加锁的成员变量，如一个商品对象的商品id等
     */
    String field() default "";
}
