package cn.brimon.framework.controller.interceptor;

import javax.servlet.http.HttpServletRequest;

public class DefaultAuthenticationInterceptor implements Interceptor {
    @Override
    public boolean intercept(HttpServletRequest request) throws Exception {
        if(request.getParameter("session").equals("xxx")){
            return true;
        }
        throw new Exception("权限校验失败！");
    }
}
