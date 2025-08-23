package tech.jabari.order.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tech.jabari.api.client.ProductFeignClient;
import tech.jabari.api.client.UserFeignClient;
import tech.jabari.api.dto.OrderDTO;
import tech.jabari.api.dto.OrderDetailDTO;
import tech.jabari.api.dto.ProductDTO;
import tech.jabari.api.dto.UserDTO;
import tech.jabari.common.result.Result;
import tech.jabari.order.entity.Order;
import tech.jabari.order.entity.OrderItem;
import tech.jabari.order.mapper.OrderItemMapper;
import tech.jabari.order.mapper.OrderMapper;

import java.util.List;

// 2. 订单服务调用链（整合用户服务与商品服务）
@Service
public class OrderService {

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private ProductFeignClient productFeignClient;
    
    @Autowired
    @Qualifier("loadBalancedRestTemplate")
    private RestTemplate restTemplate;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;
    
    // 查询订单详情（包含用户和商品信息）
    public OrderDetailDTO getOrderDetail(Long orderId) {
        // 1. 查询本地订单
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在!");
        }
        // 2. 查询订单详情
        QueryWrapper<OrderItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        List<OrderItem> orderItems = orderItemMapper.selectList(queryWrapper);
        if (orderItems == null || orderItems.isEmpty()) {
            throw new RuntimeException("订单详情不存在!");
        }
        Long productId = orderItems.get(0).getProductId();

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setUserId(order.getUserId());
        orderDTO.setProductId(productId);
        orderDTO.setAmount(order.getTotalAmount());

        // 2. 调用用户服务查询用户信息
        /*Result<UserDTO> resultUserDTO = restTemplate.getForObject(
            "http://user-service/user/" + order.getUserId(), Result.class);*/

       /* ResponseEntity<Result<UserDTO>> responseUser = restTemplate.exchange(
                "http://user-service/user/" + order.getUserId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Result<UserDTO>>() {}
        );*/

        Result<UserDTO> resultUserDTO = userFeignClient.getUser(order.getUserId());
        if (null == resultUserDTO.getData()) {
            throw new RuntimeException("用户服务-查询用户["+order.getUserId()+"]功能暂不可用！或<用户信息不存在!>");
        }
        
        // 3. 调用商品服务查询商品信息
        /*Result<ProductDTO> resultProductDTO = restTemplate.getForObject(
            "http://product-service/product/" + orderDTO.getProductId(), Result.class);*/

        /*ResponseEntity<Result<ProductDTO>> responseProduct = restTemplate.exchange(
                "http://product-service/product/" + orderDTO.getProductId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Result<ProductDTO>>() {}
        );
        Result<ProductDTO> resultProductDTO = responseProduct.getBody();*/

        Result<ProductDTO> resultProductDTO = productFeignClient.getProductById(orderDTO.getProductId());

        ProductDTO productDTO = resultProductDTO.getData();
        if (productDTO == null) {
            throw new RuntimeException("<商品服务-查询商品["+orderDTO.getProductId()+"]功能暂不可用!>或<商品信息不存在!>");
        }

        // 4. 组装结果
//        return new OrderDetailDTO(orderDTO, responseUser.getBody().getData(), productDTO);
        return new OrderDetailDTO(orderDTO, resultUserDTO.getData(), productDTO);
    }
}