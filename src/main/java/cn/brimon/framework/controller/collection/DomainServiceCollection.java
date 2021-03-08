package cn.brimon.framework.controller.collection;

import cn.brimon.framework.controller.entity.DomainService;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DomainServiceCollection {
    // <BeanName, Service>
    public static Map<String, DomainService> serviceMap = new LinkedHashMap<>();

    public static void add(Class<?> serviceClass) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        DomainService service = new DomainService(serviceClass);
        serviceMap.put(getBeanName(serviceClass), service);
    }

    private static String getBeanName(Class<?> serviceClass){
        String[] names = serviceClass.getName().split("\\.");
        return names[names.length - 1].replaceAll("Service", "");
    }

    public static DomainService get(String beanName){
        return serviceMap.get(beanName);
    }

}
