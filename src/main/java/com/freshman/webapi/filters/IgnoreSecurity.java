package com.freshman.webapi.filters;

import java.lang.annotation.*;

/**
 * 忽略需要验证登录
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreSecurity {
}
