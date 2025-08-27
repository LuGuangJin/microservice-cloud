// cloud-common/src/main/java/tech/jabari/common/security/MethodSecurityConfig.java
package tech.jabari.common.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/**
 * 全局方法安全配置
 * 启用各种权限注解支持
 */
@Configuration
@EnableGlobalMethodSecurity(
    prePostEnabled = true,      // 启用 @PreAuthorize 和 @PostAuthorize
    securedEnabled = true,      // 启用 @Secured
    jsr250Enabled = true        // 启用 @RolesAllowed (JSR-250)
)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    /**
     * 创建表达式处理器，支持自定义表达式
     */
    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = 
            new DefaultMethodSecurityExpressionHandler();
        // 可以在这里设置自定义的权限计算器
        return expressionHandler;
    }
}