package tech.jabari.user.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import tech.jabari.common.security.MethodSecurityConfig;

import javax.annotation.PostConstruct;

// 在任何业务服务中
@Slf4j
@Configuration
public class SecurityConfigValidator {
    
    @Autowired(required = false)
    private MethodSecurityConfig methodSecurityConfig;
    
    @PostConstruct
    public void validate() {
        if (methodSecurityConfig != null) {
            log.info("✅ MethodSecurityConfig 已从 cloud-common 自动加载");
        } else {
            log.warn("❌ MethodSecurityConfig 未加载，请检查 spring.factories 配置");
        }
    }
}