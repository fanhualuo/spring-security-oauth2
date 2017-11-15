package com.hehe.security.config;

import com.hehe.common.MyPasswordEncoder;
import com.hehe.common.event.CoreEventDispatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author xieqinghe .
 * @date 2017/11/14 下午4:16
 * @email xieqinghe@terminus.io
 */
@Configuration
public class SpringConfig {

    //加密类
    @Bean
    PasswordEncoder passwordEncoder() {
        return new MyPasswordEncoder();
    }
    @Bean
    CoreEventDispatcher coreEventDispatcher(){
        return new CoreEventDispatcher();
    }
}
