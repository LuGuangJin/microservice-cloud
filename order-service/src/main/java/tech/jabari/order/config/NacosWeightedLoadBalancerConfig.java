package tech.jabari.order.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/*@Configuration
@LoadBalancerClient(
        value = "user-service",
        configuration = NacosWeightedLoadBalancerConfig.class
)*/
public class NacosWeightedLoadBalancerConfig {

    @Bean
    public ReactorLoadBalancer<ServiceInstance> nacosWeightedLoadBalancer(
        Environment environment,
        LoadBalancerClientFactory factory
    ) {
        String serviceId = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        System.out.println("---WeightedLoadBalancer initialized for service: " + serviceId);
        ObjectProvider<ServiceInstanceListSupplier> lazyProvider = factory.getLazyProvider(serviceId, ServiceInstanceListSupplier.class);
        return new NacosWeightedLoadBalancer(
                lazyProvider,
            serviceId
        );
    }
}