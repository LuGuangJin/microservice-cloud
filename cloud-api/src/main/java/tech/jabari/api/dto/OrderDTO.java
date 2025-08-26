package tech.jabari.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OrderDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long userId;
    private Long productId;
    private BigDecimal amount;
    private BigDecimal totalAmount;
    private Integer status;
    private BigDecimal payAmount;
    private String orderSn;

}
