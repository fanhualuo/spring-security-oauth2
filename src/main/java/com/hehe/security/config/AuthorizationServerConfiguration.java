package com.hehe.security.config;

import com.google.common.collect.Maps;
import com.hehe.common.event.CoreEventDispatcher;
import com.hehe.common.util.RespHelper;
import com.hehe.security.config.granter.MyCompositeTokenGranter;
import com.hehe.security.config.granter.MyPasswordTokenGranter;
import com.hehe.security.model.Client;
import com.hehe.security.service.ClientReadService;
import com.hehe.security.service.MyUserDetailsService;
import com.hehe.security.service.UserReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
@Order(60)
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserReadService userReadService;

    @Autowired
    private ClientReadService clientReadService;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private CoreEventDispatcher dispatcher;

    @Autowired
    private DataSource dataSource;

    //配置客户端数据
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        clients.withClientDetails(new ClientDetailsService() {
            @Override
            public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
                Client client = RespHelper.or500(clientReadService.findByClientId(clientId));
                if (client == null) {
                    throw new NoSuchClientException("No client with requested id: " + clientId);
                }
                return client;
            }
        });

//        //配置两个客户端,一个用于password认证一个用于client认证
//        clients.inMemory().withClient("client_1")
//                .resourceIds("user")
//                .authorizedGrantTypes("authorization_code","client_credentials","password","refresh_token")
//                .scopes("select")
//                .authorities("ROLE_CLIENT")
//                .secret("1234567")
//                .and()
//                .withClient("client_2")
//                .resourceIds("user")
//                .authorizedGrantTypes("authorization_code","client_credentials","password","refresh_token")
//                .scopes("select")
//                .authorities("ROLE_CLIENT")
//                .secret("1234567");
    }

    /*
    *
    * 配置授权服务器端点的非安全特性，如令牌存储、令牌
    * 自定义，用户认证和授权类型。默认可以不用任何配置
    * 密码授权，在这种情况下，你需要提供一个{@authenticationManager}。
    * */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //密码授权的身份验证管理,必须
        endpoints.authenticationManager(this.authenticationManager);
        //自定义token存储方式
        endpoints.tokenStore(tokenStore());
        //认证方式
        endpoints.tokenGranter(tokenGranter(clientDetailsService,tokenServices()));

        //endpoints.accessTokenConverter(accessTokenConverter());
        //endpoints.userDetailsService(userDetailsService());   //自定义用户存储和操作service
        //endpoints.requestFactory(requestFactory(clientDetailsService));  //自定义应用存储和操作service
        //endpoints.tokenGranter();  //几种授权模式的自定义重写（需要一个授权管理类，和几个自定义获取token类）

    }

    /**
     * 配置授权服务器的安全性，必须配置
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        //允许表单认证
        oauthServer.allowFormAuthenticationForClients();
    }

    /**
     * token存储,这里使用redis方式存储
     * @param // accessTokenConverter
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
        TokenStore tokenStore=new RedisTokenStore(redisConnectionFactory);
        return tokenStore;
    }

    /**
     *   自定义token获取配置
     * @param
     * @return
     */
    public TokenGranter tokenGranter(ClientDetailsService clientDetailsService,  AuthorizationServerTokenServices tokenServices){
        List<TokenGranter> tokenGranters = new ArrayList<>();
        OAuth2RequestFactory requestFactory = requestFactory(clientDetailsService);

        //1，password 密码模式，重写（作为示例，其他模式都可以重写，具体可以参照spring源码）
        tokenGranters.add(new MyPasswordTokenGranter(tokenServices, clientDetailsService, requestFactory,userReadService,passwordEncoder));

        //2，authorization_code 授权码模式，spring security oauth2实现
        tokenGranters.add(new AuthorizationCodeTokenGranter(tokenServices, authorizationCodeServices(),clientDetailsService, requestFactory));

        //3，client_credentials 客户端模式，spring security oauth2实现
        tokenGranters.add(new ClientCredentialsTokenGranter(tokenServices, clientDetailsService, requestFactory));

        //4，implicit 简化模式，spring security oauth2实现
        tokenGranters.add(new ImplicitTokenGranter(tokenServices, clientDetailsService, requestFactory));

        //5, refresh_token 刷新模式 spring security oauth2实现  自己实现？
        tokenGranters.add(new RefreshTokenGranter(tokenServices, clientDetailsService, requestFactory));


        return new MyCompositeTokenGranter(tokenGranters,dispatcher);
    }

    @Bean
    public OAuth2RequestFactory requestFactory(ClientDetailsService clientDetailsService) {
        return new DefaultOAuth2RequestFactory(clientDetailsService);
    }

    /**
     *  参考spring源码实现，实现所需要类
     */
    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setReuseRefreshToken(true);
        tokenServices.setClientDetailsService(clientDetailsService);
        tokenServices.setTokenEnhancer(new TokenEnhancer() {
            @Override
            public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
                String codeSource = (String) authentication.getOAuth2Request().getExtensions().get("from");
                if (StringUtils.hasLength(codeSource)) {
                    Map<String, Object> additionalInformation = Maps.newHashMap(accessToken.getAdditionalInformation());
                    additionalInformation.put("source", codeSource);
                    ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);
                }
                return accessToken;
            }
        });
        addUserDetailsService(tokenServices, userDetailsService());
        return tokenServices;
    }

    /**
     *  参考spring源码实现，实现所需要类
     */
    private void addUserDetailsService(DefaultTokenServices tokenServices, UserDetailsService userDetailsService) {
        if (userDetailsService != null) {
            PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
            provider.setPreAuthenticatedUserDetailsService(new UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken>(
                    userDetailsService));
            tokenServices
                    .setAuthenticationManager(new ProviderManager(Arrays.asList(provider)));
        }
    }

    private UserDetailsService userDetailsService() {
        return new MyUserDetailsService();
    }

    /**
     *  参考spring源码实现
     *  code存储方式，授权码模式生成的code
     *  数据库表为 oauth_code，具体可以查看spring源码
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(dataSource);
    }
}
