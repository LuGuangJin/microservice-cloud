package tech.jabari.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.RequestRateLimiterGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * 自定义请求限流过滤器工厂
 * 重写默认的RequestRateLimiterGatewayFilterFactory，提供自定义的限流响应内容。
 *
 * @author Jabari
 * @date 2025/08/23
 * @description：根据SpringCloudGateway3.1.4版本中的父类RequestRateLimiterGatewayFilterFactory源码修改。
 */
@Component
public class CustomRequestRateLimiterGatewayFilterFactory extends RequestRateLimiterGatewayFilterFactory {

    private static final String EMPTY_KEY = "____EMPTY_KEY__";
    /**
     * 构造函数
     * @param defaultRateLimiter 默认的限流器
     * @param defaultKeyResolver 默认的Key解析器
     */
    public CustomRequestRateLimiterGatewayFilterFactory(RateLimiter defaultRateLimiter, KeyResolver defaultKeyResolver) {
        super(defaultRateLimiter, defaultKeyResolver);
    }

    /**
     * 应用限流过滤器的核心方法
     * 重写父类方法，提供自定义的限流响应处理
     * @param config 限流配置
     * @return GatewayFilter 过滤器实例
     */
    @Override
    public GatewayFilter apply(Config config) {
        // 获取配置的Key解析器和限流器，如果未配置则使用默认值
        KeyResolver resolver = getOrDefault(config.getKeyResolver(), getDefaultKeyResolver());
        RateLimiter<Object> limiter = getOrDefault(config.getRateLimiter(), getDefaultRateLimiter());
        boolean denyEmpty = getOrDefault(config.getDenyEmptyKey(), isDenyEmptyKey());
        // 使用父类的emptyKeyStatusCode作为默认值
        String emptyKeyStatusCode = getOrDefault(config.getEmptyKeyStatus(), getEmptyKeyStatusCode());

        return (exchange, chain) -> resolver.resolve(exchange).defaultIfEmpty(EMPTY_KEY).flatMap(key -> {
            // 处理空key的情况
            if (EMPTY_KEY.equals(key)) {
                if (denyEmpty) {
                    // 自定义空key的响应
                    return handleEmptyKeyResponse(exchange, emptyKeyStatusCode);
//                    return exchange.getResponse().setComplete();
                }
                return chain.filter(exchange);
            }
            
            // 获取路由ID
            String routeId = config.getRouteId();
            if (routeId == null) {
                // 从exchange中获取路由信息
                routeId = exchange.getAttribute("gatewayRouteId");
            }
            
            // 执行限流判断
            return limiter.isAllowed(routeId, key).flatMap(response -> {
                // 将限流响应头添加到HTTP响应中
                for (Map.Entry<String, String> header : response.getHeaders().entrySet()) {
                    exchange.getResponse().getHeaders().add(header.getKey(), header.getValue());
                }

                // 如果请求被允许，继续执行过滤器链
                if (response.isAllowed()) {
                    return chain.filter(exchange);
                }

                // 如果请求被拒绝，返回自定义的限流响应
                return handleRateLimitResponse(exchange, config);
            });
        });
    }

    /**
     * 处理空key的响应
     * @param exchange ServerWebExchange对象
     * @param emptyKeyStatusCode 状态码
     * @return Mono<Void>
     */
    private Mono<Void> handleEmptyKeyResponse(org.springframework.web.server.ServerWebExchange exchange, String emptyKeyStatusCode) {
        ServerHttpResponse response = exchange.getResponse();
        
        // 解析并设置状态码
        HttpStatus status = HttpStatus.valueOf(HttpStatus.valueOf(emptyKeyStatusCode).value());
        response.setStatusCode(status);
        
        // 设置自定义响应内容
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String responseBody = "{\"code\":" + status.value() + ",\"message\":\"请求标识不能为空\"}";
        
        return response.writeWith(Mono.just(response.bufferFactory().wrap(responseBody.getBytes())));
    }

    /**
     * 处理限流响应
     * @param exchange ServerWebExchange对象
     * @param config 限流配置
     * @return Mono<Void>
     */
    private Mono<Void> handleRateLimitResponse(org.springframework.web.server.ServerWebExchange exchange, Config config) {
        ServerHttpResponse response = exchange.getResponse();
        
        // 设置响应状态码（默认为429）
        HttpStatus status = config.getStatusCode() != null ? config.getStatusCode() : HttpStatus.TOO_MANY_REQUESTS;
        response.setStatusCode(status);
        
        // 设置响应内容类型为JSON
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        
        // 设置自定义响应内容
        String responseBody = "{\"code\":" + status.value() + ",\"message\":\"0请求过于频繁，请稍后再试！\"}";

        return response.writeWith(Mono.just(response.bufferFactory().wrap(responseBody.getBytes())));
    }

    /**
     * 获取配置值的工具方法，如果配置值为null则返回默认值
     * @param configValue 配置值
     * @param defaultValue 默认值
     * @param <T> 类型参数
     * @return 配置值或默认值
     */
    private <T> T getOrDefault(T configValue, T defaultValue) {
        return (configValue != null) ? configValue : defaultValue;
    }
}
