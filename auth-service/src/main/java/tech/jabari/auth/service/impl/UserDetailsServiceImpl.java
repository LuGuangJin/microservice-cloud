package tech.jabari.auth.service.impl;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tech.jabari.api.client.UserFeignClient;
import tech.jabari.api.dto.AuthUserDTO;
import tech.jabari.common.security.UserAuth;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserFeignClient userFeignClient;
    
    public UserDetailsServiceImpl(UserFeignClient userFeignClient) {
        this.userFeignClient = userFeignClient;
    }
    
    @Override
    public UserAuth loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询用户信息
        AuthUserDTO user = userFeignClient.getUserByUsername(username);
        if (null == user) {
            throw new UsernameNotFoundException("用户不存在："+username);
        }
        // 查询用户角色
        List<String> roles = userFeignClient.selectRolesByUserId(user.getId());
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());


        UserAuth userDetail = new UserAuth(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                authorities,
                user.getStatus() == 1);
        return userDetail;
    }
}