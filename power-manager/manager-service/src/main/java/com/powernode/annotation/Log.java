package com.powernode.annotation;

import java.lang.annotation.*;

/**
 * 自定义日志注解
 * @author 杜波
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Log {

    /**
     * 动作
     * @return
     */
    String operation() default "";

}