package cn.brimon.framework.controller.interceptor;

import javax.servlet.http.HttpServletRequest;

public interface Interceptor {
    public boolean intercept(HttpServletRequest request) throws Exception;
}
