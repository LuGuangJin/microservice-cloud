package tech.jabari.auth.service;

import tech.jabari.common.result.Result;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthService {
    Result<String> login(String username, String password, HttpServletResponse response);
    Result<Void> logout(HttpServletRequest request);
    Result<String> refreshToken(String oldToken);
}