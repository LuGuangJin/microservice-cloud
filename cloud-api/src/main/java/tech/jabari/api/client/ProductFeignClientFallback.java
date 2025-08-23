package tech.jabari.api.client;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.jabari.api.dto.ProductDTO;
import tech.jabari.common.result.Result;

@Component
@Slf4j
public class ProductFeignClientFallback implements ProductFeignClient{

    public Result<ProductDTO> getProductById(Long id) {
        log.error("......商品服务-查询商品[{}]功能暂不可用！", id);
        return Result.fail("商品服务-查询商品功能暂不可用！");
    }
}
