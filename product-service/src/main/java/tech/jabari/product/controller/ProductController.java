package tech.jabari.product.controller;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jabari.api.dto.ProductDTO;
import tech.jabari.common.result.Result;
import tech.jabari.product.service.ProductService;


@RestController
@RequestMapping("/product")
@Api(tags = "商品服务")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * 根据id查询商品信息
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询商品信息")
    @SentinelResource(
            value = "getProduct",
            blockHandler = "productBlockHandler"
    )
    public Result<ProductDTO> getProductById(@PathVariable("id") Long id) {
        return productService.getProductById(id);
    }



    public Result<ProductDTO> productBlockHandler(Long id, BlockException ex) {
        return Result.fail(429,"商品查询过于频繁，请稍后再试!");
    }






}
