package tech.jabari.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ProductDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private BigDecimal price; // 单价

}
