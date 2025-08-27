package tech.jabari.common.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import static tech.jabari.common.constant.CommonConstants.*;

/**
 * 认证过滤器
 * 作用：从请求头中获取用户信息，并设置到 SecurityContextHolder 中，以便后续的认证和授权。
 */
public class AuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        System.out.println("-----------AuthorizationFilter.doFilterInternal() begin:");
        try {
            String userId = request.getHeader(REQUEST_HEADER_USER_ID);
            String username = request.getHeader(REQUEST_HEADER_USER_NAME);
            String enabledStr = request.getHeader(REQUEST_HEADER_USER_ENABLED);
            String authoritiesHeader = request.getHeader(REQUEST_HEADER_USER_AUTHORITIES);

            if (userId != null && username != null) {
                Collection<? extends GrantedAuthority> authorities = parseAuthorities(authoritiesHeader);
                boolean enabled = Boolean.parseBoolean(enabledStr);
                
                UserAuth userAuth = new UserAuth(
                    Long.parseLong(userId),username, "", authorities , enabled
                );
                
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userAuth, null, authorities
                );
//                authentication.setAuthenticated(enabled);
                
                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("---Authentication set to: " + authentication);
            }
        } catch (Exception e) {
            logger.error("Failed to set authentication from headers: " + e.getMessage());
            e.printStackTrace();
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        
        filterChain.doFilter(request, response);
    }

    private Collection<? extends GrantedAuthority> parseAuthorities(String authoritiesHeader) {
        if (StringUtils.hasText(authoritiesHeader)) {
            return Arrays.stream(authoritiesHeader.split(","))
                    .map(String::trim)
                    .filter(auth -> !auth.isEmpty())
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}