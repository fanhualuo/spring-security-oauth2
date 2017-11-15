package com.hehe.security.service;

import com.hehe.common.model.Response;
import com.hehe.security.model.Client;

/**
 * @author xieqinghe .
 * @date 2017/11/15 上午10:17
 * @email xieqinghe@terminus.io
 */
public interface ClientReadService {
    /**
     * 按照id查找
     * @param  id
     * @return  Response<Client>
     */
    Response<Client> findById(Long id);

    /**
     * 按照clientI查找
     * @param  clientId
     * @return  Response<Client>
     */
    Response<Client> findByClientId(String clientId);
}
