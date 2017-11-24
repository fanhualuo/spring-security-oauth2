package com.hehe.security.config;

import com.hehe.security.config.store.MyRedisTokenStore;
import com.hehe.security.service.ClientReadService;
import com.hehe.security.service.MyCilentDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;


/**
 * @author xieqinghe .
 * @date 2017/11/13 下午1:38
 * @email xieqinghe@terminus.io
 * 认证服务配置
 * 继承AuthorizationServerConfigurerAdapter类，
 * 添加@EnableAuthorizationServer注解
 * 主要是/oauth/token自定义配置、token存储方式等
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private ClientReadService clientReadService;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    //配置客户端数据
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(new MyCilentDetailsService(clientReadService));
    }


    /**
     * 配置授权服务器的安全性，必须配置
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        //允许表单认证
        oauthServer.allowFormAuthenticationForClients();
    }

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Override // 配置框架应用上述实现
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        //密码授权的身份验证管理,必须(没有这个参数，spring自带授权没有password模式，参看源码)
        endpoints.authenticationManager(authenticationManager);
        //自定义token存储方式
        endpoints.tokenStore(tokenStore());
        //endpoints.userApprovalHandler()
    }

    /**
     * token存储,这里使用自己实现的redis方式存储
     * @param
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
        TokenStore tokenStore=new MyRedisTokenStore(redisConnectionFactory);
        return tokenStore;
    }

}
