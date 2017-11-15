package com.hehe.security.service;

import com.hehe.common.model.Response;
import com.hehe.security.model.User;


/**
 * @author xieqinghe .
 * @date 2017/11/14 下午4:35
 * @email xieqinghe@terminus.io
 */
public interface UserReadService {

    Response<User> findById(Long id);

    Response<User> findByIdentity(String identity);

    Response<User> findByPhone(String phone);

    Response<User> findByEmail(String email);

    Response<User> findByUserName(String username);

}
