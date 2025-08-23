package tech.jabari.api.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tech.jabari.api.dto.ProductDTO;
import tech.jabari.common.result.Result;

@FeignClient(name = "product-service",
        path = "/product",
        fallback = ProductFeignClientFallback.class // 降级处理的类
)
public interface ProductFeignClient {


    @GetMapping("/{id}")
    Result<ProductDTO> getProductById(@PathVariable("id") Long id);

}
