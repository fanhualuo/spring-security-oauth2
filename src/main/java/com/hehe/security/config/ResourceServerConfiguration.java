package com.hehe.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.util.matcher.RequestMatcher;


/**
 * @author xieqinghe .
 * @date 2017/11/13 下午1:43
 * @email xieqinghe@terminus.io
 * 资源服务配置
 * 继承ResourceServerConfigurerAdapter类
 * @EnableResourceServer   标注配置
 * @EnableGlobalMethodSecurity(prePostEnabled = true)   开启Spring Security的注解（加不加都可以？？）
 */
@Order(62)
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        // @formatter:off
        resources.resourceId("open").stateless(true);
        // @formatter:on
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        // @formatter:off
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/v1/**").access("#oauth2.hasScope('select')")
                .antMatchers("/v2/**").permitAll();

        // @formatter:on
    }

}