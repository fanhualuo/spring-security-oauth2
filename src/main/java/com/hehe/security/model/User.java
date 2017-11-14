package com.hehe.security.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户信息，三方都可用于登录
 * @author xieqinghe .
 * @date 2017/11/14 下午4:43
 * @email xieqinghe@terminus.io
 */
@Data
public class User implements Serializable {
    private static final long serialVersionUID = 284692345177465429L;
    private Long id;
    private String username;
    private String email;
    private String phoneNumber;
    private String password;
}
