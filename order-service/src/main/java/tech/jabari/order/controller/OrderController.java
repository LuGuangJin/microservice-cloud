package tech.jabari.order.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import tech.jabari.api.dto.OrderDetailDTO;
import tech.jabari.common.result.Result;
import tech.jabari.order.service.OrderService;

import java.util.List;
import java.util.Random;

// 1. 基于DiscoveryClient的服务发现（订单服务）
@RestController
@RequestMapping("/order")
@Api(tags = "订单服务")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;
    
    @Autowired
    private DiscoveryClient discoveryClient;  // Nacos服务发现客户端
    @Autowired
    @Qualifier("loadBalancedRestTemplate")
    private RestTemplate restTemplate;  // 负载均衡的RestTemplate

    @Autowired
    @Qualifier("directRestTemplate")
    private RestTemplate directRestTemplate;  // 直接调用的RestTemplate





    // 注入负载均衡客户端
//    @Autowired
//    private LoadBalancerClient loadBalancerClient;
    
    @GetMapping("/user/{id}")
    @ApiOperation("订单服务查询用户信息-ip：port方式")
    public Result getUser(@PathVariable Long id) {
        // 获取user-service的所有实例
        List<ServiceInstance> instances = discoveryClient.getInstances("user-service");
        // 检查是否有可用的服务实例
        if (instances == null || instances.isEmpty()) {
            throw new RuntimeException("未找到可用的 user-service 实例");
        }
        // 简单选择第一个实例（实际需结合负载均衡）
        // 生成随机整数
        int index = new Random().nextInt(instances.size());
        System.out.println("--------当前实例的随机数："+index);
        ServiceInstance instance = instances.get(index);

        // 检查实例是否为 null
        if (instance == null) {
            throw new RuntimeException("获取的服务实例为 null");
        }
        // 检查主机名和端口是否有效
        if (instance.getHost() == null || instance.getHost().isEmpty()) {
            throw new RuntimeException("服务实例主机名无效");
        }

        if (instance.getPort() <= 0) {
            throw new RuntimeException("服务实例端口无效");
        }
        // 构造请求URL：http://ip:port/user/{id}
        String url = String.format("http://%s:%s/user/%d",
                instance.getHost(), instance.getPort(), id);

        System.out.println("--------当前实例的URL："+url);
        
        // 调用用户服务
        Object result = directRestTemplate.getForObject(url, Result.class);
        if (result == null) {
            throw new RuntimeException("调用用户服务返回结果为 null");
        }
        return (Result)result;
    }



    // 优化后的服务调用（直接使用服务名）
    @GetMapping("/userOptimize/{id}")
    @ApiOperation("优化后的订单服务获取用户信息-直接使用服务名方式")
    public Result getUserOptimize(@PathVariable Long id) {
        // 服务名直接替代IP:Port，由负载均衡自动解析
        String url = "http://user-service/user/{id}";
        return restTemplate.getForObject(url, Result.class,id);
    }


    /**
     * 查看订单详情
     * @param id - 订单id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("查看订单详情")
    @SentinelResource(
            value = "getOrder",  // 资源名
            blockHandler = "handleBlock",  // 限流处理方法
            fallback = "getOrderDetailFallback"  // 整体降级方法
    )
    public Result<OrderDetailDTO> getOrderDetail(@PathVariable Long id) {
        return Result.success(orderService.getOrderDetail(id));
    }


    // 限流兜底方法（参数需与原方法一致，末尾加BlockException）
    public Result<OrderDetailDTO> handleBlock(Long id, BlockException e) {
        return Result.fail(429,"订单详情接口请求过于频繁，请稍后再试！");
    }

    // 全链路降级兜底方法
    public Result<OrderDetailDTO> getOrderDetailFallback(Long orderId, Throwable t) {
        log.error("查询订单详情失败", t);
        return Result.fail("查询订单详情服务繁忙，请稍后重试!");
    }


    /**
     * 查询登录用户的订单列表信息
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("查询登录用户的订单列表信息")
    public Result<List<OrderDetailDTO>> getOrderList() {
        return Result.success(orderService.getOrderList());
    }




}