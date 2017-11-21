package com.hehe.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xieqinghe .
 * @date 2017/11/7 上午11:33
 * @email xieqinghe@terminus.io
 */
@RestController
public class TestController {

    @GetMapping("/v2/test1")
    public String success(){
        return "/v2/test1 success";
    }

    @GetMapping("/v1/test1")
    public String test2(){
        return "/v1/test1 success ";
    }
}
