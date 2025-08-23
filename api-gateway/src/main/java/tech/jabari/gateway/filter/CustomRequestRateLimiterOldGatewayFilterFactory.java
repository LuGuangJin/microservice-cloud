package tech.jabari.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.RequestRateLimiterGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 自定义请求限流过滤器工厂
 * 用于替代默认的RequestRateLimiterGatewayFilterFactory，提供更灵活的限流控制和自定义响应
 * @author Jabari
 * @date 2025/8/23
 * 说明：这是针对 Spring Cloud Gateway 3.1.4版本的特定实现的写法！！！
 */
@Component
@Slf4j
public class CustomRequestRateLimiterOldGatewayFilterFactory extends RequestRateLimiterGatewayFilterFactory {

    // 手动定义常量，用于在exchange属性中存储限流结果
    public static final String ALLOWED_ATTR = "rateLimit.allowed";
    public static final String REMAINING_ATTR = "rateLimit.remaining";

    /**
     * 构造函数
     * @param defaultRateLimiter 默认的限流器
     * @param defaultKeyResolver 默认的Key解析器
     */
    public CustomRequestRateLimiterOldGatewayFilterFactory(RateLimiter defaultRateLimiter, KeyResolver defaultKeyResolver) {
        super(defaultRateLimiter, defaultKeyResolver);
    }

    /**
     * 应用限流过滤器的核心方法
     * @param config 限流配置
     * @return GatewayFilter 过滤器实例
     */
    @Override
    public GatewayFilter apply(Config config) {
        // 确保配置了限流Key和RateLimiter，如果未配置则使用默认值
        KeyResolver keyResolver = config.getKeyResolver() != null ? config.getKeyResolver() : getDefaultKeyResolver();
        RateLimiter rateLimiter = config.getRateLimiter() != null ? config.getRateLimiter() : getDefaultRateLimiter();

        // 返回一个GatewayFilter实现
        return (exchange, chain) -> {
            // 获取限流Key（如IP地址、用户ID等）
            return keyResolver.resolve(exchange).flatMap(key -> {
                // 执行限流判断，检查当前请求是否被允许
                return rateLimiter.isAllowed(config.getRouteId(), key).flatMap(response -> {
                    // 使用反射获取Response对象的限流状态信息
                    boolean allowed = true;
                    int remaining = 0;

                    try {
                        // 尝试调用isAllowed方法获取是否允许的状态
                        try {
                            Method isAllowedMethod = response.getClass().getMethod("isAllowed");
                            allowed = (Boolean) isAllowedMethod.invoke(response);
                            log.debug("------A01.调用isAllowed()方法，获取限流结果："+allowed);
                            System.out.println("------A01.调用isAllowed()方法，获取限流结果："+allowed);
                        } catch (NoSuchMethodException e) {
                            // 如果没有isAllowed方法，尝试直接访问allowed字段
                            log.debug("------A02.没有isAllowed()方法！");
                            System.out.println("------A02.没有isAllowed()方法！");
                            try {
                                Field allowedField = response.getClass().getDeclaredField("allowed");
                                allowedField.setAccessible(true);
                                allowed = allowedField.getBoolean(response);
                                log.debug("------A03.allowed属性，值为："+allowed);
                                System.out.println("------A03.allowed属性，值为："+allowed);

                            } catch (NoSuchFieldException ex) {
                                // 如果也没有allowed字段，默认允许请求通过
                                allowed = true;
                                log.debug("------A04.没有allowed属性，默认设置为：true");
                                System.out.println("------A04.allowed属性，默认设置为：true");
                            }
                        }

                        // 尝试获取剩余请求数信息 ---  实际上没有作用！因为在SpringCloudGateway3.1.4版本中，根本不存在这个方法和属性 ！ - Jabari Lu ，2025/08/23 18:51
                        try {
                            Method getRemainingMethod = response.getClass().getMethod("getRemaining");
                            remaining = (Integer) getRemainingMethod.invoke(response);
                            log.debug("------B01.调用getRemaining()方法，获取剩余请求数："+remaining);
                            System.out.println("------B01.调用getRemaining()方法，获取剩余请求数："+remaining);
                        } catch (NoSuchMethodException e) {
                            // 尝试直接访问remaining字段
                            try {
                                Field remainingField = response.getClass().getDeclaredField("remaining");
                                remainingField.setAccessible(true);
                                remaining = remainingField.getInt(response);
                                log.debug("------B02.remaining属性，值为："+remaining);
                                System.out.println("------B02.remaining属性，值为："+remaining);
                            } catch (NoSuchFieldException ex) {
                                // 如果没有remaining信息，使用默认值0
                                remaining = 0;
                                log.debug("------B03.没有remaining属性，默认设置为：0");
                                System.out.println("------B03.remaining属性，默认设置为：0");
                            }
                        }
                    } catch (Exception e) {
                        // 如果所有尝试都失败，默认允许请求通过，避免因反射失败导致服务不可用
                        allowed = true;
                        remaining = 0;
                        log.debug("------C01.反射失败，默认设置为：true");
                        System.out.println("------C01.反射失败，默认设置为：true");
                    }

                    // 将限流结果存入exchange的属性中，供后续过滤器使用
                    exchange.getAttributes().put(ALLOWED_ATTR, allowed);
                    exchange.getAttributes().put(REMAINING_ATTR, remaining);

                    // 如果触发限流（请求不被允许），手动抛出异常
                    if (!allowed) {
                        System.err.println("----请求过于频繁，触发限流！");
                        return Mono.error(new RuntimeException("请求过于频繁"));
                    }

                    // 未触发限流，继续执行过滤器链
                    return chain.filter(exchange);
                });
            }).<Void>onErrorResume(throwable -> {
                // 捕获限流异常，返回自定义的响应
                ServerHttpResponse response = exchange.getResponse();
                // 设置响应状态码为429 (Too Many Requests)
                response.setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                // 设置响应体格式为JSON
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                // 自定义响应内容
                String responseBody = "{\"code\":429,\"message\":\"1请求过于频繁，请稍后再试！\"}";
                // 写入响应体并返回
                return response.writeWith(
                        Mono.just(response.bufferFactory().wrap(responseBody.getBytes()))
                );
            });
        };
    }
}
