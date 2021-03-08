package cn.brimon.framework.collection;

import cn.brimon.framework.Initializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;

public class ClassCollection {
    public static List<Class<?>> classList = new LinkedList<Class<?>>();
    private static Logger logger = LoggerFactory.getLogger(ClassCollection.class);

    public static void addClass(String className) {
        try {
            Class<?> aClass = Class.forName(className);
            classList.add(Class.forName(className));
            if(Initializer.class.isAssignableFrom(aClass)){
                InitializerCollection.add(aClass);
            }
        } catch (Throwable throwable) {
            logger.error("Error When Initializing Scan Class: " + className);
            logger.error(throwable.getMessage());
            throwable.printStackTrace();
        }
    }
}
