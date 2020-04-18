package com.neptune.sentinel.zookeeper;

import com.alibaba.csp.sentinel.config.SentinelConfig;
import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.zookeeper.ZookeeperDataSource;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.neptune.sentinel.RuleType;
import com.neptune.sentinel.ZookeeperConfigUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ZookeeperDataSourceConfig implements InitializingBean {

    @Value("${zookeeper.config.connectString}")
    private String zkServerAddress;

    @Autowired
    private CuratorFramework zkClient;

    private static final String flowRuleZkPath = ZookeeperConfigUtil.getPath(SentinelConfig.getAppName(), RuleType.FLOW);
    private static final String degradeRuleZkPath = ZookeeperConfigUtil.getPath(SentinelConfig.getAppName(), RuleType.DEGRADE);

    @Override
    public void afterPropertiesSet() {

        ReadableDataSource<String, List<FlowRule>> zookeeperDataSource = new ZookeeperDataSource<>(
                zkServerAddress,
                flowRuleZkPath,
                source -> {
                    log.info("流控规则发生变化，flowRuleZkPath:" + flowRuleZkPath + "，规则内容：" + source);
                    return JSON.parseObject(source, new TypeReference<List<FlowRule>>() {
                    });
                });
        FlowRuleManager.register2Property(zookeeperDataSource.getProperty());


//        ReadableDataSource<String, List<DegradeRule>> degradeRuleDataSource = new ZookeeperDataSource<>(
//                zkServerAddress,
//                degradeRuleZkPath, source -> {
//            Map<Long, DegradeRule> rules = JSON.parseObject(source,
//                    new TypeReference<Map<Long, DegradeRule>>() {
//                    });
//            if (rules == null || rules.isEmpty()) {
//                return new ArrayList<>();
//            }
//            List<DegradeRule> list = new ArrayList<>(rules.values());
//            log.info("降级规则发生变化，degradeRuleZkPath:" + degradeRuleZkPath + "，规则内容：" + JSON.toJSONString(list));
//            return list;
//        });
//        DegradeRuleManager.register2Property(degradeRuleDataSource.getProperty());


        ReadableDataSource<String, List<DegradeRule>> degradeRuleDataSource = new ZookeeperDataSource<>(
                zkServerAddress,
                degradeRuleZkPath,
                source -> {
                    log.info("降级规则发生变化，flowRuleZkPath:" + flowRuleZkPath + "，规则内容：" + source);
                    return JSON.parseObject(source, new TypeReference<List<DegradeRule>>() {
                    });
                });
        DegradeRuleManager.register2Property(degradeRuleDataSource.getProperty());
    }
}

