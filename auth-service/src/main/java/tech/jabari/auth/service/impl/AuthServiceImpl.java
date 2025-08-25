package tech.jabari.auth.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tech.jabari.auth.entity.UserAuth;
import tech.jabari.auth.service.AuthService;
import tech.jabari.auth.util.JwtTokenUtil;
import tech.jabari.common.result.Result;

import javax.servlet.http.HttpServletRequest;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    
    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }
    
    @Override
    public Result<String> login(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = 
            new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserAuth userAuth = (UserAuth) (authentication.getPrincipal());
        String token = jwtTokenUtil.generateToken(userAuth, userAuth.getUserId());
        return Result.success(token);
    }
    
    @Override
    public Result logout(HttpServletRequest request) {
        String token = jwtTokenUtil.getTokenFromRequest(request);
        jwtTokenUtil.invalidateToken(token);
        return Result.success("退出成功");
    }
    
    @Override
    public Result<String> refreshToken(String oldToken) {
        String refreshedToken = jwtTokenUtil.refreshToken(oldToken);
        return Result.success(refreshedToken);
    }
}