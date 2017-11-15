package com.hehe.security.service;

import com.hehe.common.model.Response;
import com.hehe.security.model.User;
import org.springframework.stereotype.Service;

/**
 * @author xieqinghe .
 * @date 2017/11/15 下午4:29
 * @email xieqinghe@terminus.io
 */
@Service
public class UserReadServiceImp implements UserReadService {

    public static User user=new User();
    static {
        user.setId(1l);
        user.setUsername("hehe");
        user.setEmail("qinghe101@qq.com");
        user.setPhone("15854026443");
        user.setPassword("8fc4@cf1636f1278457e3dfae");

    }


    @Override
    public Response<User> findById(Long id) {
        if (id==1){
            return Response.ok(user);
        }
        return Response.fail("error");
    }

    @Override
    public Response<User> findByIdentity(String identity) {
        if (identity.equals("hehe")||identity.equals("qinghe101@qq.com")
                ||identity.equals("15854026443")){
            return Response.ok(user);
        }
        return Response.fail("error");
    }

    @Override
    public Response<User> findByPhone(String phone) {
        if (phone.equals("15854026443")){
            return Response.ok(user);
        }
        return Response.fail("error");
    }

    @Override
    public Response<User> findByEmail(String email) {
        if (email.equals("qinghe101@qq.com")){
            return Response.ok(user);
        }
        return Response.fail("error");
    }

    @Override
    public Response<User> findByUserName(String username) {
        return null;
    }
}
