package tech.jabari.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("product_category")
public class ProductCategory {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long parentId;
    private String name;
    private Integer level;
    private Integer sort;
}