// api-gateway/src/main/java/tech/jabari/gateway/config/GatewaySecurityConfig.java
package tech.jabari.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * API Gateway 安全配置 - 使用 WebFlux Security
 */
@Configuration
@EnableWebFluxSecurity  // 注意：这里是EnableWebFluxSecurity，不是EnableWebSecurity
public class GatewaySecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        System.out.println("------网关安全配置类（WebFlux）。。。。。。。。。");

        return http
                .csrf(csrf -> csrf.disable())  // 禁用 CSRF
                .authorizeExchange(exchanges -> exchanges.anyExchange().permitAll() )
//                        .pathMatchers("/api/auth/login").permitAll()
//                        .pathMatchers("/public/**", "/actuator/health").permitAll()
//                        .pathMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**").permitAll()
//                        .anyExchange().authenticated()  // 注意：这里是 anyExchange()
//                )
                .build();
    }
}