package tech.jabari.order.config;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/*@Configuration
@LoadBalancerClient(
    value = "user-service",
    configuration = LoadBalancerConfig.class
)*/
public class LoadBalancerConfig {
    
    @Bean
    public ReactorLoadBalancer<ServiceInstance> randomLoadBalancer(
        Environment environment,
        LoadBalancerClientFactory factory
    ) {
        String serviceId = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        System.out.println("---LoadBalancer initialized for service: " + serviceId);
        // 权重负载均衡
        /*return new NacosWeightedLoadBalancer(
                factory.getLazyProvider(serviceId, ServiceInstanceListSupplier.class),
                serviceId
        );*/
        // 轮询策略  - 默认的负载均衡策略
        return new RoundRobinLoadBalancer(
                factory.getLazyProvider(serviceId, ServiceInstanceListSupplier.class),
                serviceId
        );
        // 随机策略
        /*return new RandomLoadBalancer(
                factory.getLazyProvider(serviceId, ServiceInstanceListSupplier.class),
                serviceId
        );*/
    }



}