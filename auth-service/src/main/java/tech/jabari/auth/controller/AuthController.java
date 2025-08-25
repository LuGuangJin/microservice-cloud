package tech.jabari.auth.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import tech.jabari.auth.service.AuthService;
import tech.jabari.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
@Api(tags = "认证服务")
public class AuthController {

    @Autowired
    private AuthService authService;
    
    @PostMapping("/login")
    @ApiOperation("用户登录")
    public Result<String> login(@RequestParam String username, @RequestParam String password) {
        return authService.login(username, password);
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
}