package tech.jabari.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 商品服务启动类
 *
 */
@SpringBootApplication
//@ComponentScan(basePackages = {"tech.jabari.product","tech.jabari.common"})
@EnableDiscoveryClient
//@EnableGlobalMethodSecurity(prePostEnabled = true) // 开启方法权限控制 , prePostEnabled = true 表示开启权限控注解的PreAuthorize
public class ProductApp
{
    public static void main( String[] args )
    {
        ConfigurableApplicationContext ctx = SpringApplication.run(ProductApp.class, args);
        String port = ctx.getEnvironment().getProperty("server.port");
        System.out.println( "商品服务启动成功，端口为："+ port );
    }
}
