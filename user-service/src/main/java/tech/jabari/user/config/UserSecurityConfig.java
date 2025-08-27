package tech.jabari.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tech.jabari.common.security.AuthorizationFilter;

// Spring Security 5.6+ 推荐写法 （注意：5.7开始，删除了@EnableGlobalMethodSecurity注解）
@Configuration
@EnableWebSecurity
@EnableMethodSecurity // 开启方法级安全注解
// 旧版本（Spring Security 5.6-）写法（已过时）
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class UserSecurityConfig /*extends WebSecurityConfigurerAdapter*/ {


    private final AuthorizationFilter authorizationFilter; // 注入 cloud-common 中的过滤器


    public UserSecurityConfig(AuthorizationFilter authorityFilter) {
        this.authorizationFilter = authorityFilter;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("-------UserSecurityConfig->securityFilterChain()........begin:");
        http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .authorizeHttpRequests(authz -> authz
                .antMatchers("/user/username", "/user/roles/{id}","/user/info1/{id}").permitAll() // 这些URL是允许匿名访问的
                .anyRequest().authenticated() // 其它的URL需要身份验证
        )// 在 Spring Security过滤器链之前添加自定义过滤器
        .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);
        System.out.println("-------UserSecurityConfig->securityFilterChain()........End!");
        return http.build();
    }

    /*@Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println("-------UserSecurityConfig()...........begin:");
        http
            // 在 Spring Security 过滤器链之前添加自定义过滤器
            .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class)
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/user/username", "/user/roles/{id}","/user/info/{id}").permitAll()// ,"/user/info/{id}"
            .anyRequest().authenticated();
    }*/

}
