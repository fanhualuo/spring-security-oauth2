package com.hehe.security.config;

import com.hehe.security.config.provider.CustomAuthenticationProvider;
import com.hehe.security.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;


/**
 * @author xieqinghe .
 * @date 2017/11/7 上午11:51
 * @email xieqinghe@terminus.io
 */
@Configuration
// Security配置注解，标识其是spring security配置文件
@EnableWebSecurity
//允许进入页面方法前检验
// @EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http
                .authorizeRequests()
                .antMatchers("/oauth/**").permitAll()
                .antMatchers("/login").permitAll()
                .and()
                .formLogin()
                .loginProcessingUrl("/login")
                .loginPage("/login.html")
                .permitAll();
//                .and()
//                .httpBasic();

    }


    //重写框架的UserDetailsService接口，自定义用户查询接口
    @Override
    @Bean
    public UserDetailsService userDetailsService() {
        return new MyUserDetailsService();
    }

//    @Autowired
//    private CustomAuthenticationProvider authenticationProvider;

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        //将验证过程交给自定义验证工具
//        auth.authenticationProvider(authenticationProvider);
//    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
