package tech.jabari.common.interceptor;

import cn.hutool.core.util.StrUtil;
import tech.jabari.common.util.UserContext;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static tech.jabari.common.constant.CommonConstants.REQUEST_HEADER_USER_INFO;

/**
 * 用户信息拦截器 （拦截请求头中的用户信息）
 */
public class UserInfoInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1、获取请求头中的用户信息
        String userInfo = request.getHeader(REQUEST_HEADER_USER_INFO);
        // 2、将用户信息放入ThreadLocal中
        if (StrUtil.isNotBlank(userInfo)) {
            // 1、将用户信息放入ThreadLocal中
            UserContext.setUser(Long.valueOf(userInfo));
        }
        // 3、放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //移除用户信息
        UserContext.removeUser();
    }
}