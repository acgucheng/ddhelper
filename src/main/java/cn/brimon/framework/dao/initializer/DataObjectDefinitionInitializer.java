package cn.brimon.framework.dao.initializer;

import cn.brimon.framework.ClassScanner;
import cn.brimon.framework.Initializer;
import cn.brimon.framework.collection.ClassCollection;
import cn.brimon.framework.dao.annotation.DataObject;
import cn.brimon.framework.dao.collection.DataObjectCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DataObjectDefinitionInitializer implements Initializer {
    Logger logger = LoggerFactory.getLogger(DataObjectDefinitionInitializer.class);
    @Override
    public void init() throws Exception {
        List<Class<?>> classList = ClassCollection.classList;
        System.out.println("Initializing data object");
        for(Class<?> aClass : classList){
            if(aClass.isAnnotationPresent(DataObject.class)){
                System.out.println("Find Data Object " + aClass.getName());
                DataObjectCollection.add(aClass);
            }
        }
    }
}
