package com.neptune.sentinel.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.neptune.sentinel.config.HandlerExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class HelloController {

    @RequestMapping("saveHello")
    @SentinelResource(value = "saveHello", blockHandler = "saveHelloHandleException", blockHandlerClass = {HandlerExceptionUtil.class})
    public void saveHello() {
        for (int i = 0; i <1000 ; i++) {
            this.testSaveHello();
        }
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>  saveHello");
    }


    @RequestMapping("testSaveHello")
    @SentinelResource(value = "testSaveHello", blockHandler = "testSaveHelloBlockHandler")
    public void testSaveHello() {
//        for (int i = 0; i <1000 ; i++) {
//            this.saveHello();
//        }
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>  testSaveHello");
    }

    public void testSaveHelloBlockHandler(BlockException e) {
        log.error("testSaveHelloBlockHandler: [{}],[{}]", e, e.getMessage());
    }
}
