package tech.jabari.order;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * 订单服务启动类
 */
@SpringBootApplication
@ComponentScan(basePackages = {"tech.jabari.order", "tech.jabari.common","tech.jabari.api.client"})
@EnableDiscoveryClient // 启用服务注册与发现
@EnableFeignClients(basePackages = {"tech.jabari.api.client"}) // 启用Feign客户端
public class OrderApp {
    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(OrderApp.class, args);
        // 检查LoadBalancer相关Bean是否存在
        /*System.out.println("---LoadBalancer Bean: " +
                ctx.getBean(ReactorLoadBalancer.class));*/
        String port = ctx.getEnvironment().getProperty("server.port");
        System.out.println("-----订单服务启动成功，端口号为：" + port);
    }
}
