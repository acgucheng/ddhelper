package cn.brimon.framework.controller.annotation;

import cn.brimon.framework.controller.interceptor.DefaultAuthenticationInterceptor;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DomainSecurity {
    public Class<?>[] value() default {DefaultAuthenticationInterceptor.class};
}
