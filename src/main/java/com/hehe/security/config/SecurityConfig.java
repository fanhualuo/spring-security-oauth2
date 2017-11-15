package com.hehe.security.config;

import com.hehe.security.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


/**
 * @author xieqinghe .
 * @date 2017/11/7 上午11:51
 * @email xieqinghe@terminus.io
 */
@Order(50)
@Configuration
// Security配置注解，标识其是spring security配置文件
@EnableWebSecurity
//开启Spring Security的注解
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static MyUserDetailsService myUserDetailsService=new MyUserDetailsService();

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/oauth/token").permitAll()
                .antMatchers("/oauth/authorize").permitAll()
                .and()
                .csrf().disable();

        //csrf()  添加CSRF的支持。使用时默认会激活此选项。
        //disable()   新的配置会覆盖此配置
        //antMatchers("/oauth/token").permitAll()    允许所有用户访问的路径，不设置拦截
    }


    //重写框架的UserDetailsService接口，覆盖
    @Override
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
                // 通过用户名获取用户信息
                UserDetails user = myUserDetailsService.loadUserByUsername(name);
                if (user!=null){
                    return user;
                } else {
                    throw new UsernameNotFoundException("用户[" + name + "]不存在");
                }
            }
        };

    }


//    @Override
//    @Bean
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }


}
