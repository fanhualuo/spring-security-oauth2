package com.hehe.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import java.util.HashMap;
import java.util.Map;

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
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    /*
    *
    * 配置授权服务器端点的非安全特性，如令牌存储、令牌
    * 自定义，用户认证和授权类型。默认可以不用任何配置
    * 密码授权，在这种情况下，你需要提供一个{@链接authenticationManager }。
    * */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //密码授权的身份验证管理
        endpoints.authenticationManager(this.authenticationManager);

        //endpoints.accessTokenConverter(accessTokenConverter());


        //自定义token存储方式
        endpoints.tokenStore(tokenStore());

        //endpoints.userDetailsService(userDetailsService());   //自定义用户存储和操作service
        //endpoints.requestFactory(requestFactory(clientDetailsService));  //自定义应用存储和操作service
        //endpoints.tokenGranter();  //几种授权模式的自定义重写（需要一个授权管理类，和几个自定义获取token类）

    }

    //配置授权服务器的安全性
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.tokenKeyAccess("isAnonymous() || hasAuthority('ROLE_TRUSTED_CLIENT')");
        oauthServer.checkTokenAccess("hasAuthority('ROLE_TRUSTED_CLIENT')");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

    }

    /**
     * token存储,这里使用redis方式存储
     *
     * @param // accessTokenConverter
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
        TokenStore tokenStore=new RedisTokenStore(redisConnectionFactory);
        return tokenStore;
    }
}
