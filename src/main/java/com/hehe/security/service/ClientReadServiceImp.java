package com.hehe.security.service;

import com.google.common.collect.Sets;
import com.hehe.common.model.Response;
import com.hehe.security.model.Client;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.Collections;
import java.util.List;

/**
 * 应用信息，建议保存在数据库中，缓存到redis或内存当中
 * @author xieqinghe .
 * @date 2017/11/15 上午10:43
 * @email xieqinghe@terminus.io
 */
@Service
public class ClientReadServiceImp implements ClientReadService {

    private static Client client1=new Client();
    private static Client client2=new Client();
    static {
        /**
         *应用名、秘钥凭证、应用id、应用角色、应用授权范围、应用权限、可重定向url、资源、access_token有效期(N)、refresh_token有效期(N)
         */
        client1.setClientId("client_1");
        client1.setClientSecret("1234567");
        client1.setId(1L);
        client1.setAuthorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_CLIENT")));
        client1.setAuthorizedGrantTypes(Sets.newHashSet("authorization_code","client_credentials","password","refresh_token"));
        client1.setScope(Sets.newHashSet("select","update","create"));
        client1.setRegisteredRedirectUri(Sets.newHashSet("http://www.baidu.com"));
        client1.setResourceIds(Sets.newHashSet("resource_1"));
        client1.setAccessTokenValiditySeconds(1209600);
        client1.setRefreshTokenValiditySeconds(31536000);

        client2.setClientId("client_2");
        client2.setClientSecret("1234567");
        client2.setId(2L);
        client2.setAuthorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_CLIENT")));
        client2.setAuthorizedGrantTypes(Sets.newHashSet("authorization_code","client_credentials","password","refresh_token"));
        client2.setScope(Sets.newHashSet("select","update","create"));
        client2.setRegisteredRedirectUri(Sets.newHashSet("http://www.baidu.com"));
        client2.setResourceIds(Sets.newHashSet("resource_2"));
        client2.setAccessTokenValiditySeconds(1209600);
        client2.setRefreshTokenValiditySeconds(31536000);
    }



    @Override
    public Response<Client> findByClientId(String clientId) {
        Response<Client> resp=new Response<>();
        if (clientId.equals("client_1")){
            resp.setResult(client1);
        }else if (clientId.equals("client_2")){
            resp.setResult(client2);
        }else {
            return Response.fail("没有应用信息");
        }
        return resp;
    }

    @Override
    public Response<Client> findById(Long id) {
        Response<Client> resp=new Response<>();
        if (id.equals(1)){
            resp.setResult(client1);
        }else if (id.equals(2)){
            resp.setResult(client2);
        }else {
            return Response.fail("没有应用信息");
        }
        return resp;
    }
}
