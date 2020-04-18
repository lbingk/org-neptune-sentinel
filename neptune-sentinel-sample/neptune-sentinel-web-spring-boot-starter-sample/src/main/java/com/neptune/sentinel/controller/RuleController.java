package com.neptune.sentinel.controller;

import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Slf4j
public class RuleController {

    @RequestMapping("flowData")
    public List<FlowRule> flowDate() {
        List<FlowRule> flowRuleList = FlowRuleManager.getRules();
        log.info("获取流控规则：[{}]", JSON.toJSONString(flowRuleList));
        return flowRuleList;
    }

}
