package tech.jabari.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.jabari.product.entity.ProductCategory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductCategoryMapper extends BaseMapper<ProductCategory> {
}