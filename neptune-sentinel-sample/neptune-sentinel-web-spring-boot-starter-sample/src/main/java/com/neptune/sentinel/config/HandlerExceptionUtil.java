package com.neptune.sentinel.config;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HandlerExceptionUtil {

    public static void saveHelloHandleException(BlockException ex) {
        log.error(">>>>>>>>>>>>>>>>>>>>>>>>>   正在处理拦截异常；[{}]", JSON.toJSONString(ex));
    }
}
