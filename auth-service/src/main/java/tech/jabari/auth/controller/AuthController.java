package tech.jabari.auth.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.jabari.auth.service.AuthService;
import tech.jabari.common.result.Result;
import tech.jabari.common.security.SecurityUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
@Api(tags = "认证服务")
public class AuthController {

    @Autowired
    private AuthService authService;
    
    @PostMapping("/login")
    @ApiOperation("用户登录")
    public Result<String> login(@RequestParam("username") String username,
                                @RequestParam("password") String password,
                                HttpServletResponse response) {
        return authService.login(username, password,response);
    }
    
    @PostMapping("/logout")
    @ApiOperation("用户退出")
    public Result logout(HttpServletRequest request) {
        return authService.logout(request);
    }
    
    @PostMapping("/refresh")
    @ApiOperation("刷新令牌")
    public Result<String> refreshToken(@RequestParam String token) {
        return authService.refreshToken(token);
    }


    @GetMapping("/security")
    @PreAuthorize("isAuthenticated()") // 测试方法级安全
    public String testSecurity() {
        return "Security working! User: " + SecurityUtils.getCurrentUsername();
    }

    @GetMapping("/public")
    public String publicEndpoint() {
        return "This is public";
    }

}