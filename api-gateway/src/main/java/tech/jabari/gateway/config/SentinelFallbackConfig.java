package tech.jabari.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

//@Component
public class SentinelFallbackConfig {

    /**
     * 初始化自定义降级逻辑（适配Spring Cloud Gateway响应式模型）
     */
    @PostConstruct
    public void initFallbackHandler() {
        GatewayCallbackManager.setBlockHandler((exchange, throwable) -> {
            // 1. 构建响应对象
            String message = String.format(
                "{\"code\":503,\"message\":\"服务熔断保护中\",\"detail\":\"%s\",\"timestamp\":\"%s\"}",
                throwable.getMessage(),
                LocalDateTime.now()
            );

            // 2. 创建 ServerResponse 并返回
            return ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(message);
        });
    }
}
