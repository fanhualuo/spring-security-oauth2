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

        endpoints.accessTokenConverter(accessTokenConverter());


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
     * token converter
     *
     * @return
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter() {
            /***
             * 重写增强token方法,用于自定义一些token返回的信息
             */
            @Override
            public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
                String userName = authentication.getUserAuthentication().getName();
                User user = (User) authentication.getUserAuthentication().getPrincipal();// 与登录时候放进去的UserDetail实现类一直查看link{SecurityConfiguration}
                /** 自定义一些token属性 ***/
                final Map<String, Object> additionalInformation = new HashMap<>();
                additionalInformation.put("userName", userName);
                additionalInformation.put("roles", user.getAuthorities());
                ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);
                OAuth2AccessToken enhancedToken = super.enhance(accessToken, authentication);
                return enhancedToken;
            }

        };
        accessTokenConverter.setSigningKey("123");// 测试用,资源服务使用相同的字符达到一个对称加密的效果,生产时候使用RSA非对称加密方式
        return accessTokenConverter;
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
