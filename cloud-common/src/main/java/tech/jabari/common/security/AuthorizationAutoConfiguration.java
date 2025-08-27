package tech.jabari.common.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.Servlet;

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)  // 只在Servlet环境中生效
@ConditionalOnClass({ Servlet.class, DispatcherServlet.class })  // 确保是Spring MVC环境
public class AuthorizationAutoConfiguration {

    @Bean
    public AuthorizationFilter authorizationFilter() {
        return new AuthorizationFilter();
    }

    // 注册一个独立的Servlet过滤器
    /*@Bean
    public FilterRegistrationBean<AuthorizationFilter> authorizationFilterRegistration(AuthorizationFilter filter) {
        FilterRegistrationBean<AuthorizationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);// 设置最高优先级
        return registrationBean;
    }*/


    // 将过滤器添加到 Spring Security 的过滤器链
    /*@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthorizationFilter authorizationFilter) throws Exception {
        http
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .anyRequest().authenticated()
            .and()
            // 将我的过滤器添加到Spring Security过滤器链中
            .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);
        System.out.println("--------成功将AuthorizationFilter过滤器添加到Spring Security过滤器链！");
        return http.build();
    }*/
}