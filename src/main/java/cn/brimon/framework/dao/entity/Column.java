package cn.brimon.framework.dao.entity;

import java.lang.reflect.Field;

public class Column {
    private String columnName;
    private Object columnValue;
    //标识primaryKey，若不为主键，则为null值
    private Integer primaryKeyIndex;
    //该列对应的field
    private Field field;
    private Column column;

    public String getColumnName() {
        return columnName;
    }

    public Column setColumnName(String columnName) {
        this.columnName = columnName;
        return this;
    }

    public Object getColumnValue() {
        return columnValue;
    }

    public Column setColumnValue(Object columnValue) {
        this.columnValue = columnValue;
        return this;
    }

    public Integer getPrimaryKeyIndex() {
        return primaryKeyIndex;
    }

    public Column setPrimaryKeyIndex(Integer primaryKeyIndex) {
        this.primaryKeyIndex = primaryKeyIndex;
        return this;
    }

    public Field getField() {
        return field;
    }

    public Column setField(Field field) {
        this.field = field;
        return this;
    }

    public static Column copyOf(Column column){
        return new Column().setColumnName(column.columnName)
                .setColumnValue(column.columnValue)
                .setField(column.field)
                .setPrimaryKeyIndex(column.primaryKeyIndex);
    }

    @Override
    public String toString() {
        return "Column{" +
                "columnName='" + columnName + '\'' +
                ", columnValue=" + columnValue +
                ", primaryKeyIndex=" + primaryKeyIndex +
                ", field=" + field +
                ", column=" + column +
                '}';
    }
}
