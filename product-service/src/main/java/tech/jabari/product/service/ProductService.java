package tech.jabari.product.service;

import tech.jabari.api.dto.ProductDTO;
import tech.jabari.common.result.Result;
import tech.jabari.product.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface ProductService extends IService<Product> {
    Result<ProductDTO> getProductById(Long id);
    Result<List<ProductDTO>> listProducts();
    Result<List<ProductDTO>> searchProducts(String keyword);
    Result<ProductDTO> createProduct(Product product);
    Result<ProductDTO> updateProduct(Long id, Product product);
    Result<Void> deleteProduct(Long id);
}