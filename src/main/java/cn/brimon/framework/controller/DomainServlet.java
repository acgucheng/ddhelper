package cn.brimon.framework.controller;

import cn.brimon.framework.controller.collection.DomainServiceCollection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class DomainServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] paths = req.getRequestURI().replace(req.getContextPath(),"").split("\\/");
        String beanName = paths[paths.length - 2];
        String operation = paths[paths.length - 1];
        try {
            DomainServiceCollection.get(beanName).invokeOperation(operation, req.getParameterMap());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
