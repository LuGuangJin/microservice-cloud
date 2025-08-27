package tech.jabari.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

@EnableFeignClients(basePackages = "tech.jabari.api.client")
//@ComponentScan(basePackages = {"tech.jabari.auth", "tech.jabari.common"})
//@MapperScan("tech.jabari.auth.mapper")
@SpringBootApplication
@EnableDiscoveryClient
public class AuthApp {
    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(AuthApp.class, args);
        String port = ctx.getEnvironment().getProperty("server.port");
        System.out.println("-----认证服务启动成功，端口号为："+port );
    }
}
