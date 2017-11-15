package com.hehe.security.service;

import com.hehe.security.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lxg
 * on 2017/2/20.
 */
public class MyUserDetailsService implements UserDetailsService {


    /**
     * 根据用户名获取登录用户信息
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user=new User();
        user.setId(1l);
        user.setUsername("hehe");
        user.setEmail("qinghe101@qq.com");
        user.setPhone("15854026443");
        user.setPassword("8fc4@cf1636f1278457e3dfae");
        if(user == null) {
            throw new UsernameNotFoundException("CustomUserDetailsServiceImpl.notFound"+ new Object[]{username}+"Username {0} not found");
        } else {
            List<GrantedAuthority> authorities = new ArrayList();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            boolean isLocked =false;
            if(!isLocked) {

            }

            return new org.springframework.security.core.userdetails.User(user.getId().toString(), user.getPassword(), true, true, true, !isLocked, Collections.unmodifiableList(authorities));
        }

    }
}
