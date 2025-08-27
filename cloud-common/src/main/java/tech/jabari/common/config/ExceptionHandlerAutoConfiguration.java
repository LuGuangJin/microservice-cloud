package tech.jabari.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.jabari.common.exception.GlobalExceptionHandler;

/**
 * 异常处理自动配置
 */
@Configuration
public class ExceptionHandlerAutoConfiguration {

    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }
}