package tech.jabari.order.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;
import tech.jabari.order.util.SmoothedWeightedRandomUtils;

import java.util.List;
import java.util.stream.Collectors;

public class NacosWeightedLoadBalancer implements ReactorServiceInstanceLoadBalancer {

    private final ObjectProvider<ServiceInstanceListSupplier> supplierProvider;
    private final String serviceId;

    public NacosWeightedLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> supplierProvider, String serviceId) {
        this.supplierProvider = supplierProvider;
        this.serviceId = serviceId;
    }

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        ServiceInstanceListSupplier supplier = supplierProvider.getIfAvailable();
        return supplier.get(request).next()
            .map(instances -> {
                // 过滤掉权重<=0的实例
                List<ServiceInstance> validInstances = instances.stream()
                    .filter(instance -> {
                        double weight = Double.parseDouble(
                            instance.getMetadata().getOrDefault("nacos.weight", "1")
                        );
                        return weight > 0;
                    })
                    .collect(Collectors.toList());

                if (validInstances.isEmpty()) {
                    return new EmptyResponse();
                }

                // 按权重随机选择
                double[] weights = validInstances.stream()
                    .mapToDouble(instance -> 
                        Double.parseDouble(instance.getMetadata().getOrDefault("nacos.weight", "1"))
                    )
                    .toArray();
                // 遍历weights
                System.out.printf("------weights length: %d, weights: %s \n" , weights.length,java.util.Arrays.toString(weights));
//                int index = WeightedRandomUtils.choose(weights);
                int index = SmoothedWeightedRandomUtils.choose(weights);
                System.out.println("------chose index: " + index);
                return new DefaultResponse(validInstances.get(index));
            });
    }
}