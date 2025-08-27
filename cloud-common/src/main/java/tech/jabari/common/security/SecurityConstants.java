package tech.jabari.common.security;

/**
 * 安全相关常量
 */
public final class SecurityConstants {
    
    // 公共端点路径 - 所有服务共用
    public static final String[] PUBLIC_ENDPOINTS = {
        "/auth/login",
        "/auth/register",
        "/auth/logout",
        "/actuator/health",
        "/actuator/info",
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/swagger-ui.html",
        "/swagger-resources/**",
        "/webjars/**",
        "/error"
    };

    // 角色常量
    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_MANAGER = "MANAGER";

    // 权限常量
    public static final String PERMISSION_READ = "READ";
    public static final String PERMISSION_WRITE = "WRITE";
    public static final String PERMISSION_DELETE = "DELETE";
    public static final String PERMISSION_UPDATE = "UPDATE";

    // 特定权限前缀
    public static final String PRODUCT_READ = "PRODUCT_READ";
    public static final String PRODUCT_WRITE = "PRODUCT_WRITE";
    public static final String USER_READ = "USER_READ";
    public static final String USER_WRITE = "USER_WRITE";
    public static final String ORDER_READ = "ORDER_READ";
    public static final String ORDER_WRITE = "ORDER_WRITE";

    private SecurityConstants() {
        // 工具类，防止实例化
    }
}