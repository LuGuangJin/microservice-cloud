package tech.jabari.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "jabari.user")
public class UserProperties {
    private Integer maxAmount;
}