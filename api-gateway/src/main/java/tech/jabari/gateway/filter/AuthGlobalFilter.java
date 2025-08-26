package tech.jabari.gateway.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import tech.jabari.gateway.config.AuthProperties;
import tech.jabari.gateway.util.JwtTokenUtil;

import java.util.function.Consumer;

import static tech.jabari.common.constant.CommonConstants.REQUEST_HEADER_USER_INFO;

/**
 * 认证过滤器 （全局过滤器）
 */
@Component
@EnableConfigurationProperties(AuthProperties.class)
@RequiredArgsConstructor
public class AuthGlobalFilter implements GlobalFilter, Ordered {
    private final JwtTokenUtil jwtTool;
    private final AuthProperties authProperties;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Value("${jwt.token-header}")
    private String jwtTokenHeader;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1、判断是否要拦截
        ServerHttpRequest request = exchange.getRequest();
        if (isExclude(request.getPath().toString())) {
            //无需拦截；直接放行
            System.out.println("------AuthGlobalFilter.filter(): ---> 无需拦截:"+request.getPath().toString());
            return chain.filter(exchange);
        }
        //2、获取token
        String token = request.getHeaders().getFirst(jwtTokenHeader);
        System.out.println("------AuthGlobalFilter.filter(): ---> token = " + token);
        //3、校验token
        Long userId = null;
        try {
            userId = jwtTool.getUserIdFromToken(token);
            //4、传递用户信息 （保存到请求头中，传递给下游的微服务）
        	System.out.println("------AuthGlobalFilter.filter(): ---> userId = " + userId);
            String userInfo= userId.toString();
            ServerWebExchange webExchange= exchange.mutate ().request(
                    new Consumer<ServerHttpRequest.Builder>() {
                        @Override
                        public void accept(ServerHttpRequest.Builder builder) {
                            builder.header(REQUEST_HEADER_USER_INFO, userInfo);
                        }
                    }).build();
        } catch (Exception e) {
            e.printStackTrace();
            //token校验失败
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        
        //5、放行
        return chain.filter(exchange);
    }

    /**
     * 判断是否需要拦截
     * @param path - 当前请求路径
     * @return true：不需要拦截；false：需要拦截。
     */
    private boolean isExclude(String path) {
        for (String excludePath : authProperties.getExcludePaths()) {
            if (antPathMatcher.match(excludePath, path)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}