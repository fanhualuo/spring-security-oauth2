package com.hehe.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;


/**
 * @author xieqinghe .
 * @date 2017/11/13 下午1:43
 * @email xieqinghe@terminus.io
 * 资源服务配置
 * 继承ResourceServerConfigurerAdapter类
 * @EnableResourceServer   标注配置
 * @EnableGlobalMethodSecurity(prePostEnabled = true)   开启Spring Security的注解（加不加都可以？？）
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId("resource_1").stateless(false);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .authorizeRequests()
                .antMatchers("/v1/**").access("#oauth2.hasScope('select')")
                .antMatchers("/v2/**").permitAll();
    }

}