package cn.brimon.framework.eventsource.annotation;

import cn.brimon.framework.eventsource.DomainEvent;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {
    public Class<? extends DomainEvent> value();
}
