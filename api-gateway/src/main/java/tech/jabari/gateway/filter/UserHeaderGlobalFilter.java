/*
package tech.jabari.gateway.filter;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import tech.jabari.gateway.util.JwtTokenUtil;

import static tech.jabari.common.constant.CommonConstants.*;

@Component
public class UserHeaderGlobalFilter implements GlobalFilter, Ordered {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.token-header}")
    private String jwtTokenHeader;



    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        
        // 从Authorization头获取token
        String authHeader = request.getHeaders().getFirst(jwtTokenHeader);
        System.out.println("------UserHeaderGlobalFilter.filter(): ---> authHeader = " + authHeader);
        if (authHeader != null) {
            String userIdFromToken = jwtTokenUtil.getUserIdFromToken(authHeader).toString();
            String username = jwtTokenUtil.getUsernameFromToken(authHeader);
            String roles = jwtTokenUtil.getRolesFromToken(authHeader);
            System.out.println("---userId:"+userIdFromToken+",username:"+username+",roles:"+roles);
            if (StrUtil.isNotBlank(userIdFromToken) && StrUtil.isNotBlank(username)) {
                // 添加用户信息到请求头中
                ServerHttpRequest.Builder requestBuilder = request.mutate();
                requestBuilder.header(REQUEST_HEADER_USER_ID, userIdFromToken);
                requestBuilder.header(REQUEST_HEADER_USER_NAME, username);
                requestBuilder.header(REQUEST_HEADER_USER_AUTHORITIES, roles);
                requestBuilder.header(REQUEST_HEADER_USER_ENABLED, "true");
                
                return chain.filter(exchange.mutate().request(requestBuilder.build()).build());
            }
        }
        
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}*/
