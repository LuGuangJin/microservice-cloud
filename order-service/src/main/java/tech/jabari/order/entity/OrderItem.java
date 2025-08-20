package tech.jabari.order.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@TableName("order_item")
public class OrderItem {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long orderId;
    private Long productId;
    private String productName;
    private BigDecimal productPrice;
    private Integer quantity;
    private BigDecimal totalPrice;
}