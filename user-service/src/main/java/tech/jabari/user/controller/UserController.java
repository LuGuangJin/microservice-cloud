package tech.jabari.user.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.jabari.api.dto.AuthUserDTO;
import tech.jabari.api.dto.UserDTO;
import tech.jabari.common.result.Result;
import tech.jabari.common.security.SecurityUtils;
import tech.jabari.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

// 控制器
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

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


    @GetMapping("/info/{id}")
    @ApiOperation("获取用户信息（db）")
//    @PreAuthorize("permitAll()")
    /* @PreAuthorize 生效的前提是：
    请求未被HttpSecurity过滤器链拦截（要么被 permitAll() 允许，要么已通过认证）。
    */
//    @PreAuthorize("isAuthenticated()") // isAuthenticated(): 是否已经被认证了（登录用户）
    @PreAuthorize("hasRole('USER1') or authentication.principal.userId == #id") // #id
    public Result<UserDTO> getUserInfo(@PathVariable Long id) {
        System.out.printf("......用户信息接口在[%s]被调用，端口号为：%s。\n",LocalDateTime.now().toString(), port);
        boolean authenticated = SecurityUtils.getCurrentAuthentication().isAuthenticated();
        System.out.println("......用户是否已登录：" + authenticated);
        System.out.println("......用户名：" + SecurityUtils.getCurrentUsername());
        UserDTO userDTO = userService.getUserById(id);
//        return Result.success(userDTO != null ? userDTO : new UserDTO(-1L,"未找到用户名","未找到手机号"));
        return Result.success(userDTO);
    }



    @PostMapping("/username")
    @ApiOperation("获取用户信息（db）")
    AuthUserDTO getUserByUsername(@RequestParam("username") String username) {
        AuthUserDTO userDTO = userService.getUserByUsername(username);
        return userDTO;
    }

    @PostMapping("/roles/{id}")
    List<String> selectRolesByUserId(@PathVariable("id") Long id){
        List<String> roles = userService.selectRolesByUserId(id);
        return roles;
    }
    

}