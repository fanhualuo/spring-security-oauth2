package com.hehe.security.service;

import com.hehe.common.util.RespHelper;
import com.hehe.security.model.Client;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;

/**
 * @author xieqinghe .
 * @date 2017/11/17 下午4:15
 * @email xieqinghe@terminus.io
 */
public class MyCilentDetailsService implements ClientDetailsService{
    private ClientReadService clientReadService;

    public MyCilentDetailsService(ClientReadService clientReadService){
        this.clientReadService=clientReadService;
    }


    /**
     * 根据客户端id获取登录用户信息
     * @param clientId
     * @return
     * @throws NoSuchClientException
     */
    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {

        Client client = RespHelper.or500(clientReadService.findByClientId(clientId));
        if (client == null) {
            throw new NoSuchClientException("No client with requested id: " + clientId);
        }
        return client;
    }
}
