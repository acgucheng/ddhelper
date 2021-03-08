package cn.brimon.framework.dao.entity;

import cn.brimon.framework.dao.annotation.DataObject;
import cn.brimon.framework.dao.annotation.PrimaryKey;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class DataObjectDefinition {
    private Class<?> dataObjectClass;
    private String tableName;
    private List<Column> columnList = new LinkedList<>();
    private Map<String,Column> columnMap = new LinkedHashMap<>();
    public DataObjectDefinition(Class<?> dataObjectClass){
        this.dataObjectClass = dataObjectClass;
        if(!this.dataObjectClass.isAnnotationPresent(DataObject.class)) {
            throw new RuntimeException("Can't find dataObject Annotation on " +
                    dataObjectClass.getName());
        }
        this.tableName = this.dataObjectClass.getAnnotation(DataObject.class).table();
        parseColumn();
    }

    private void parseColumn(){
        Field[] fields = dataObjectClass.getDeclaredFields();
        for (Field field: fields) {
            String columnName = field.getName();
            Integer primaryKeyIndex = null;
            if (field.isAnnotationPresent(cn.brimon.framework.dao.annotation.Column.class)){
                cn.brimon.framework.dao.annotation.Column annotation = field.getAnnotation(cn.brimon.framework.dao.annotation.Column.class);
                if(!annotation.value().equals("")){
                    columnName = annotation.value();
                }
            }
            if(field.isAnnotationPresent(PrimaryKey.class)){
                primaryKeyIndex = field.getAnnotation(PrimaryKey.class).index();
            }
            columnList.add(new Column().setColumnName(columnName).setPrimaryKeyIndex(primaryKeyIndex).setField(field));
        }
    }

    public String getTableName(){
        return this.tableName;
    }

    public List<Column> getColumnsWithValue(Object dataObject){
        List<Column> retColumns = new ArrayList<>();
        for(Column column : columnList) {
            try {
                Field field = dataObject.getClass().getDeclaredField(column.getColumnName());
                // 为防止并发修改带来的accessible不一致问题。需要同步进入
                synchronized (field) {
                    boolean canAccess = field.canAccess(dataObject);
                    field.setAccessible(true);
                    Object value = field.get(dataObject);
                    // 需要过滤掉value为null的情况
                    if(value != null){
                        Column retColumn = Column.copyOf(column).setColumnValue(value);
                        retColumns.add(retColumn);
                    }
                    field.setAccessible(canAccess);
                }

            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException("Can't find field in dataObject called " + column.getColumnName());
            }
        }
        return retColumns;

    }

    private Column getColumnByColumnName(String columnName){
        for(Column column: columnList){
            if(column.getColumnName().equals(columnName)){
                return column;
            }
        }
        throw new RuntimeException("未找到数据库列为".concat(columnName).concat("的Column对象"));
    }

    public Object convertMapToObject(Map<String, Object> map){
        try {
            Object instance = dataObjectClass.getConstructor().newInstance();
            for(String key: map.keySet()){
                Column column = getColumnByColumnName(key);
                Field field = column.getField();
                synchronized (field){
                    boolean canAccess = field.canAccess(instance);
                    field.setAccessible(true);
                    field.set(instance, map.get(column.getColumnName()));
                    field.setAccessible(canAccess);
                }
            }
            return instance;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Convert Map to Object error");
    }

}
