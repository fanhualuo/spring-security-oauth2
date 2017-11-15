package com.hehe.security.event;

import com.google.common.collect.ImmutableMap;
import com.google.common.eventbus.Subscribe;
import com.hehe.common.event.EventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.stereotype.Component;

import javax.sound.sampled.Line;
import java.util.Date;

/**
 * 用户登录等操作信息写入日志
 * @author xieqinghe .
 * @date 2017/11/15 下午5:17
 * @email xieqinghe@terminus.io
 */
@Component
@Slf4j
public class UserEventLogs implements EventListener {

    @Subscribe
    public void onEveryLogin(UserEventLogsDto userEventLogsDto) {
        log.info("日志写入");
        log.info(userEventLogsDto.getTokenRequest().toString());
        log.info(userEventLogsDto.getGrant().toString());
        log.info(userEventLogsDto.getDesc());
        log.info("-------------------");

    }

}
