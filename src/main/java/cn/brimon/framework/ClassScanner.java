package cn.brimon.framework;

import cn.brimon.framework.collection.ClassCollection;

import java.io.File;

public class ClassScanner {
    public static void scan(String basePackage){
        String path = ClassScanner.class.getClassLoader().getResource("").getPath() + basePackage.replaceAll("\\.","/");
        File pathFile = new File(path);
        File[] files = pathFile.listFiles();
        for(File file : files) {
            if(file.isDirectory()){
                scan(basePackage.concat(".").concat(file.getName()));
            }else if(file.getName().endsWith(".class")){
                String className = basePackage.concat(".").concat(file.getName().replace(".class",""));
                ClassCollection.addClass(className);
            }
        }
    }
}
