package com.hehe.security.config;

import com.hehe.security.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;


/**
 * @author xieqinghe .
 * @date 2017/11/7 上午11:51
 * @email xieqinghe@terminus.io
 */
@Order(50)
@Configuration
// Security配置注解，标识其是spring security配置文件
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/oauth/**").permitAll()
                .and()
                .httpBasic().realmName("OAuth Server")
                .and()
                .csrf()
                .disable();

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

}
