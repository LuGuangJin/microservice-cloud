package tech.jabari.gateway.exception;

import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

// 全局异常处理
@Configuration
public class GatewayExceptionHandler {
    @Bean
    public ErrorWebExceptionHandler customErrorHandler() {
        return (exchange, ex) -> {
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            String message = "服务器内部错误-Gateway";

            /*if (ex instanceof JwtException) {
                status = HttpStatus.UNAUTHORIZED;
                message = "令牌无效或已过期";
            } else*/ if (ex instanceof FlowException) {
                status = HttpStatus.TOO_MANY_REQUESTS;
                message = "请求过于频繁-Gateway，请稍后再试";
            }

            exchange.getResponse().setStatusCode(status);
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
            String body = "{\"code\":" + status.value() + ",\"msg\":\"" + message + "\"}";
            System.out.println("-----------Global Exception:" + message);
            return exchange.getResponse()
                    .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(body.getBytes())));
        };
    }
}