package tech.jabari.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"tech.jabari.gateway", "tech.jabari.common"})
public class GatewayApp {
    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(GatewayApp.class, args);
        String port = ctx.getEnvironment().getProperty("server.port");
        System.out.println("-----网关服务启动成功，端口号为：" + port);
    }
}