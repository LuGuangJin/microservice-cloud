package tech.jabari.product.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jabari.api.dto.ProductDTO;
import tech.jabari.common.result.Result;


@RestController
@RequestMapping("/product")
@Api(tags = "商品服务")
public class ProductController {

    /**
     * 根据id查询商品信息
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询商品信息")
    public Result<ProductDTO> getProductById(@PathVariable("id") Long id) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(id);
        productDTO.setName("商品" + id);
        productDTO.setPrice(new java.math.BigDecimal(100));
        return Result.success(productDTO);
    }

}
