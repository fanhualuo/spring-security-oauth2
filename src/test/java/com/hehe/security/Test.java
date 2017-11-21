package com.hehe.security;

import org.springframework.security.crypto.codec.Base64;

import java.io.UnsupportedEncodingException;

/**
 * @author xieqinghe .
 * @date 2017/11/15 下午1:25
 * @email xieqinghe@terminus.io
 */
public class Test {

    public static void main(String[] args){
//        PasswordEncoder passwordEncoder=new MyPasswordEncoder();
//        System.out.println(passwordEncoder.encode("123456"));

        System.out.println(generate("client_1", "1234567"));
    }



    private static String generate(String clientId, String clientSecret) {
        String creds = String.format("%s:%s", new Object[] { clientId, clientSecret });
        try {
            return "Basic " + new String(Base64.encode(creds.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Could not convert String");
        }
    }
}
