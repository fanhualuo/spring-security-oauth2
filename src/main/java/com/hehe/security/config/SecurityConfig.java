package com.hehe.security.config;

import com.hehe.security.service.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


/**
 * @author xieqinghe .
 * @date 2017/11/7 上午11:51
 * @email xieqinghe@terminus.io
 */
@Order(50)
@Configuration
// Security配置注解，标识其是spring security配置文件
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .authorizeRequests()
                .antMatchers("/login.html").permitAll()
                .anyRequest().hasRole("ROLE_USER")
                .and()
                .exceptionHandling()
                .accessDeniedPage("/login.html?authorization_error=true")
                .and()
                // TODO: put CSRF protection back into this endpoint
                .csrf()
                .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/oauth/authorize"))
                .disable()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login.html")
                .and()
                .formLogin()
                .loginProcessingUrl("/login")
                .failureUrl("/login.html?authentication_error=true")
                .loginPage("/login.html");
        // @formatter:on
    }


    //重写框架的UserDetailsService接口，自定义用户查询接口
    @Override
    @Bean
    public UserDetailsService userDetailsService() {
        return new MyUserDetailsService();
    }

    //密码授权管理
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/webjars/**", "/images/**", "/oauth/uncache_approvals", "/oauth/cache_approvals");
    }

}
