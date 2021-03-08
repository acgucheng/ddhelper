package cn.brimon.framework.controller.initializer;

import cn.brimon.framework.Initializer;
import cn.brimon.framework.collection.ClassCollection;
import cn.brimon.framework.controller.collection.DomainServiceCollection;

public class ServiceInitializer implements Initializer {

    @Override
    public void init() throws Exception {
        for(Class<?> aClass : ClassCollection.classList){
            DomainServiceCollection.add(aClass);
        }
    }
}
