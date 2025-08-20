package tech.jabari.user.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jabari.api.dto.UserDTO;
import tech.jabari.common.result.Result;

import java.time.LocalDateTime;

// 控制器
@RestController
@RequestMapping("/user")
public class UserController {

    @Value("${server.port}")
    private String port;

    
    @GetMapping("/{id}")
    public Result<UserDTO> getUser(@PathVariable Long id) {
//        int x = 6 / 0 ;
        System.out.printf("......查询用户服务在[%s]被调用，端口号为：%s。\n",LocalDateTime.now().toString(), port);
        UserDTO user = new UserDTO();
        user.setId(id);
        user.setUsername("用户" + id);
        user.setPhone("13305678901");
       /* try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }*/
        return Result.success(user);
    }
    
    @Data // Lombok注解
    @AllArgsConstructor
    public static class User {
        private Long id;
        private String name;
        private LocalDateTime createTime;
    }
}