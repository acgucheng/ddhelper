package cn.brimon.framework.controller.entity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

public class DomainService {
    // <operationName, operation>
    private Map<String, Operation> operationMap = new LinkedHashMap<>();
    private Object service;

    public DomainService(Class<?> serviceClass) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Method[] methods = serviceClass.getMethods();
        this.service = serviceClass.getConstructor().newInstance();
        for(Method method: methods){
            operationMap.put(method.getName(), new Operation(method));
        }
    }

    public void invokeOperation(String operationName, Map<String, String[]> parameterMap) throws InvocationTargetException, IllegalAccessException {
        Operation operation = operationMap.get(operationName);
        operation.invoke(service, parameterMap);
    }

}
