package cn.brimon.framework.collection;

import cn.brimon.framework.Initializer;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

public class InitializerCollection {
    private static List<Initializer> initializers = new LinkedList<>();

    public static void add(Class<?> aClass) {
        if(Initializer.class.isAssignableFrom(aClass)){
            try{
                initializers.add((Initializer) aClass.getDeclaredConstructor().newInstance());
            }catch (InstantiationException | InvocationTargetException | NoSuchMethodException |
                    IllegalAccessException e){
                e.printStackTrace();
            }
        }
    }

    public static List<Initializer> get(){
        return initializers;
    }

}
