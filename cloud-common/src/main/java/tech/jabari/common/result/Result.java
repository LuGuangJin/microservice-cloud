package tech.jabari.common.result;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@Data
@ApiModel(description = "统一响应结果")
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "状态码", example = "200")
    private Integer code;
    
    @ApiModelProperty(value = "消息", example = "success")
    private String message;
    
    @ApiModelProperty(value = "响应数据")
    private T data;
    
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        return result;
    }
    
    public static <T> Result<T> fail(String message) {
        Result<T> result = new Result<>();
        result.setCode(500);
        result.setMessage(message);
        return result;
    }
    
    public static <T> Result<T> fail(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}