package tech.jabari.order.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    @Bean("loadBalancedRestTemplate")
    @LoadBalanced  // 开启负载均衡（自动集成Ribbon）！启用服务名解析
    public RestTemplate loadBalancedRestTemplate() {
        System.out.println("---创建RestTemplate实例，使用@LoadBalanced注解...");
        RestTemplate restTemplate = new RestTemplate();
        /*restTemplate.getInterceptors().add((request, body, execution) -> {
            System.out.println("---LoadBalancer Target URL: " + request.getURI());
            return execution.execute(request, body);
        });*/
        return restTemplate;
    }


    @Bean("directRestTemplate")
    public RestTemplate directRestTemplate() {
        return new RestTemplate();  // 用于直接IP调用的RestTemplate
    }



    /**
     * Ribbon中配置随机的负载均衡算法
     */
//    @Bean
//    public IRule ribbonRule() {
//        return new RandomRule();
//    }






}