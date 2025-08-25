package tech.jabari.api.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tech.jabari.api.dto.AuthUserDTO;
import tech.jabari.api.dto.UserDTO;
import tech.jabari.common.result.Result;

import java.util.List;


@FeignClient(name = "user-service",
        path = "/user",
        fallback = UserFeignClientFallback.class // 降级处理的类
)
public interface UserFeignClient {

    @GetMapping("/{id}")
    public Result<UserDTO> getUser(@PathVariable("id") Long id);

    @PostMapping("/username")
    AuthUserDTO getUserByUsername(@RequestParam("username") String username);

    @PostMapping("/roles/{id}")
    List<String> selectRolesByUserId(@PathVariable("id") Long id);
}
