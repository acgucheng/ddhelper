package cn.brimon.framework.controller.entity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Operation {
    private List<Parameter> paramList = new LinkedList<>();
    private Method method;

    public Operation(Method method){
        for(Parameter parameter : method.getParameters()){
            paramList.add(parameter);
        }
        this.method = method;
    }

    public void invoke(Object service, Map<String,String[]> parameterMap) throws InvocationTargetException, IllegalAccessException {
        Object[] args = new Object[paramList.size()];
        System.out.println(parameterMap);
        for(int i = 0; i < paramList.size(); i++){
            args[i] = parameterMap.get(paramList.get(i).getName())[0];
        }
        this.method.invoke(service,args);
    }

    private Object convertParam(Object obj){
        if(String.class.isAssignableFrom(obj.getClass())) return (String) obj;
        return obj;
    }
}
