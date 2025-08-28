package tech.jabari.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Swagger配置属性
 */
@Data
@ConfigurationProperties(prefix = "jabari.swagger")
public class SwaggerProperties {

    private String basePackage = "tech.jabari";

    private String title = "Swagger API文档";

    private String description = "Swagger API文档";

    private String version = "1.0";

    private String termsOfServiceUrl = "http://www.jabari.tech/termsOfServiceUrl";

    private String contactName = "Jabari";

    private String contactUrl = "http://www.jabari.tech";

    private String contactEmail = "jabari@sina.com";

}
