package cn.brimon.framework.dao.collection;

import cn.brimon.framework.dao.entity.DataObjectDefinition;

import java.util.LinkedHashMap;
import java.util.Map;

public class DataObjectCollection {
    public static Map<Class<?>, DataObjectDefinition> dataObjectDefinitionMap = new LinkedHashMap<>();
    
    public static void add(Class<?> dataObjectClass){
        DataObjectDefinition dataObjectDefinition = new DataObjectDefinition(dataObjectClass);
        dataObjectDefinitionMap.put(dataObjectClass, dataObjectDefinition);
    }

    public static DataObjectDefinition getDefinition(Class<?> aClass){
        return dataObjectDefinitionMap.get(aClass);
    }
}
