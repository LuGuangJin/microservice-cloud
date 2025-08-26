package tech.jabari.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;

// 启动类
@SpringBootApplication
// 扫描控制器所在包（默认）+ 全局异常处理器所在包
/*@ComponentScan(basePackages = {
        "tech.jabari.user",  // 控制器所在根包
        "tech.jabari.common" // 全局异常处理器所在根包
})*/
@EnableDiscoveryClient // 启用服务注册与发现
public class UserApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(UserApplication.class, args);
        String port = ctx.getEnvironment().getProperty("server.port");
        System.out.println("-----用户服务启动成功，端口号为："+port );
    }
}