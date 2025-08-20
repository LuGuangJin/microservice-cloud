package tech.jabari.api.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tech.jabari.api.dto.UserDTO;
import tech.jabari.common.result.Result;


@FeignClient(name = "user-service", path = "/user")
public interface UserFeignClient {

    @GetMapping("/{id}")
    public Result<UserDTO> getUser(@PathVariable("id") Long id);
}
