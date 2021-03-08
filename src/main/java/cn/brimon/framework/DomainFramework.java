package cn.brimon.framework;

import cn.brimon.framework.collection.InitializerCollection;
import cn.brimon.framework.controller.initializer.ServiceInitializer;
import cn.brimon.framework.dao.initializer.DataObjectDefinitionInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DomainFramework {

    private String basePackage;
    Logger logger = LoggerFactory.getLogger(DomainFramework.class);

    DomainFramework(String basePackage) {
        this.basePackage = basePackage;
    }

    public void run() throws Exception {
        // 1. 扫描包下所有类
        ClassScanner.scan(basePackage);
        // 2. 执行所有的Initializer
        doInitialize();
        new ServiceInitializer().init();
        new DataObjectDefinitionInitializer().init();
        //new ServiceInitializer().init();
    }

    public void doInitialize() {
        List<Initializer> initializers = InitializerCollection.get();
        for(Initializer initializer: initializers){
            try{
                initializer.init();
            }catch(Exception e){
                logger.error("初始化" + initializer.getClass().getName() + "失败");
                e.printStackTrace();
            }

        }
    }

    public void setBasePackage(String basePackage){
        this.basePackage = basePackage;
    }

}
