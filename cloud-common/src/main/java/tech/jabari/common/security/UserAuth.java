package tech.jabari.common.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class UserAuth extends User {
    private static final long serialVersionUID = 1L;

    private Long userId;
    private boolean enabled;

    public UserAuth(Long userId, String username, String password, Collection<? extends GrantedAuthority> authorities,boolean enabled) {
        super(username,password, authorities);
        this.userId = userId;
        this.enabled = enabled;
    }

    public Long getUserId() {
        return userId;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }

}
