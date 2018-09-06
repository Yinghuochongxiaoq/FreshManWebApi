package com.freshman.webapi.filters;

import java.lang.annotation.*;

/**
 * 用于标识用户实体类入参
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentUser {
}
