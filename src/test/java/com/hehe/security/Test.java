package com.hehe.security;

import com.hehe.common.MyPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author xieqinghe .
 * @date 2017/11/15 下午1:25
 * @email xieqinghe@terminus.io
 */
public class Test {

    public static void main(String[] args){
        PasswordEncoder passwordEncoder=new MyPasswordEncoder();
        System.out.println(passwordEncoder.encode("123456"));
    }
}
