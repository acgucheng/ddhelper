package cn.brimon.framework.dao;

import cn.brimon.framework.dao.collection.DataObjectCollection;
import cn.brimon.framework.dao.entity.Column;
import cn.brimon.framework.dao.entity.DataObjectDefinition;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasicDao<T> {

    private static SqlSessionFactory sqlSessionFactory;
    private DataObjectDefinition dataObjectDefinition;

    static{
        try {
            InputStream resourceAsStream = Resources.getResourceAsStream("mybatis-config.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BasicDao(Class<?> dataObjectClass){
        this.dataObjectDefinition = DataObjectCollection.getDefinition(dataObjectClass);
        if(dataObjectDefinition == null)
            throw new RuntimeException("未找到类" + dataObjectClass.getName() + "的定义");
    }

    public void update(Object dataObject){
        // 1. 初始化数据格式
        List<Column> columns = dataObjectDefinition.getColumnsWithValue(dataObject);
        String tableName = dataObjectDefinition.getTableName();
        System.out.println(columns);
        List<Column> updateColumn = new ArrayList<>();
        List<Column> primaryKeyColumn = new ArrayList<>();
        for(Column column: columns) {
            if (column.getPrimaryKeyIndex() != null && column.getColumnValue()!=null) {
                primaryKeyColumn.add(column);
            } else if(column.getColumnValue() != null){
                updateColumn.add(column);
            }
        }
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("tableName", tableName);
        parameterMap.put("primaryKeyColumns", primaryKeyColumn);
        parameterMap.put("updateColumns", updateColumn);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        // 2.执行update语句
        sqlSession.update("BasicDaoMapper.update", parameterMap);
        sqlSession.commit();
        sqlSession.close();
    }

    public List<T> findByTemplate(Object template){
        // 1. 初始化数据格式
        List<Column> columns = dataObjectDefinition.getColumnsWithValue(template);
        String tableName = dataObjectDefinition.getTableName();
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("tableName", tableName);
        parameterMap.put("columns", columns);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        // 2.执行find语句
        List<Map<String, Object>> result = sqlSession.selectList("BasicDaoMapper.findByTemplate", parameterMap);
        List<T> retList = new ArrayList<>();
        for(Map<String, Object> valueMap : result){
            T object = (T)dataObjectDefinition.convertMapToObject(valueMap);
            retList.add(object);
        }
        return retList;
    }

    public void insert(Object template){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Map<String, Object> parameterMap = new HashMap<>();
        List<Column> columns = dataObjectDefinition.getColumnsWithValue(template);
        String tableName = dataObjectDefinition.getTableName();
        parameterMap.put("tableName",tableName);
        System.out.println("columns: " + columns);
        parameterMap.put("columns",columns);
        sqlSession.insert("BasicDaoMapper.insert", parameterMap);
        sqlSession.commit();
        sqlSession.close();
    }

    public void delete(T template){
        // 1. 初始化数据格式
        List<Column> columns = dataObjectDefinition.getColumnsWithValue(template);
        String tableName = dataObjectDefinition.getTableName();
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("tableName", tableName);
        parameterMap.put("columns", columns);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        sqlSession.delete("BasicDaoMapper.delete", parameterMap);
        sqlSession.commit();
        sqlSession.close();
    }

}
