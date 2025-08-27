package tech.jabari.product.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import tech.jabari.api.dto.ProductDTO;
import tech.jabari.common.result.Result;
import tech.jabari.common.util.UserContext;
import tech.jabari.product.entity.Product;
import tech.jabari.product.mapper.ProductMapper;
import tech.jabari.product.service.ProductService;

import java.util.List;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {


    @Override
    public Result<ProductDTO> getProductById(Long id) {
        System.out.println("----getProductById(): " + id + ",userId = " + UserContext.getUser());
        Product product = this.getById(id);
        if (product != null) {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(product.getId());
            productDTO.setName(product.getName());
            productDTO.setPrice(product.getPrice());
            return Result.success(productDTO);
        }
        return Result.success( null);
    }

    @Override
    public Result<List<ProductDTO>> listProducts() {
        return null;
    }

    @Override
    public Result<List<ProductDTO>> searchProducts(String keyword) {
        return null;
    }

    @Override
    public Result<ProductDTO> createProduct(Product product) {
        return null;
    }

    @Override
    public Result<ProductDTO> updateProduct(Long id, Product product) {
        return null;
    }

    @Override
    public Result<Void> deleteProduct(Long id) {
        return null;
    }
}
