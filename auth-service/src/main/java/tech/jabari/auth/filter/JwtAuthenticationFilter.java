/*
package tech.jabari.auth.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import tech.jabari.auth.config.AuthProperties;
import tech.jabari.auth.util.JwtTokenUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

*/
/**
 * JWT认证过滤器
 * 用于验证JWT令牌并设置认证信息
 *//*

@Slf4j
@Component
@EnableConfigurationProperties(AuthProperties.class)
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthProperties authProperties;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        // 检查是否是白名单路径
        if (isWhiteListed(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 从请求头中获取JWT令牌
        String jwtToken = getJwtFromRequest(request);

        // 验证令牌
        if (StringUtils.hasText(jwtToken)) {
            try {
                // 从JWT中提取用户名
                String username = jwtTokenUtil.getUsernameFromToken(jwtToken);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // 加载用户信息
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    // 验证令牌是否有效
                    if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                        // 创建认证对象
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities()
                                );

                        // 设置认证详情
                        authentication.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request)
                        );

                        // 设置认证信息到SecurityContext
                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        log.debug("用户 {} 认证成功", username);
                    }
                }
            } catch (Exception e) {
                log.error("JWT认证失败: {}", e.getMessage());
                // 这里可以返回具体的错误信息，但不要抛出异常以免中断过滤链
            }
        } else {
            log.debug("请求未包含JWT令牌: {}", requestURI);
        }

        // 继续执行过滤链
        filterChain.doFilter(request, response);
    }

    */
/**
     * 从请求头中获取JWT令牌
     *//*

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = jwtTokenUtil.getTokenFromRequest(request);
        
        if (null != bearerToken) {
            return bearerToken;
        }
        // 也支持从参数中获取token（可选）
        String tokenParam = request.getParameter("token");
        if (StringUtils.hasText(tokenParam)) {
            return tokenParam;
        }
        return null;
    }

    */
/**
     * 检查请求路径是否在白名单中
     *//*

    private boolean isWhiteListed(String requestURI) {
        */
/*for (String whitePath : WHITE_LIST) {
            if (requestURI.startsWith(whitePath)) {
                return true;
            }
        }
        return false;*//*

        return isExclude(requestURI);
    }

    */
/**
     * 重写shouldNotFilter方法，明确指定哪些路径不过滤
     *//*

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        */
/*return path.startsWith("/auth/login") ||
               path.startsWith("/auth/register") ||
               path.startsWith("/auth/refresh") ||
               path.startsWith("/swagger") ||
               path.startsWith("/v2/api-docs") ||
               path.startsWith("/v3/api-docs") ||
               path.startsWith("/webjars") ||
               path.startsWith("/actuator");*//*


        return isExclude(path);
    }



    private boolean isExclude(String path) {
        for (String excludePath : authProperties.getExcludePaths()) {
            if (antPathMatcher.match(excludePath, path)) {
                return true;
            }
        }
        return false;
    }
}*/
