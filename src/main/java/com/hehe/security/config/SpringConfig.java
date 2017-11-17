package com.hehe.security.config;

import com.hehe.common.MyPasswordEncoder;
import com.hehe.common.event.CoreEventDispatcher;
import com.hehe.security.service.ClientReadService;
import com.hehe.security.service.MyCilentDetailsService;
import com.hehe.security.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetailsService;

/**
 * @author xieqinghe .
 * @date 2017/11/14 下午4:16
 * @email xieqinghe@terminus.io
 */
@Configuration
public class SpringConfig {

    @Autowired
    private ClientReadService clientReadService;

    //加密类
    @Bean
    PasswordEncoder passwordEncoder() {
        return new MyPasswordEncoder();
    }
    //eventBus
    @Bean
    CoreEventDispatcher coreEventDispatcher(){
        return new CoreEventDispatcher();
    }
//    //客户端service
//    @Bean
//    ClientDetailsService clientDetailsService(){
//        return new MyCilentDetailsService(clientReadService);
//    }
//
//    //用户service
//    @Bean
//    UserDetailsService userDetailsService(){
//        return new MyUserDetailsService();
//    }
}
