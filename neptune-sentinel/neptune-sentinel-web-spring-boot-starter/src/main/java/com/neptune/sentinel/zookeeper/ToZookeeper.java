package com.neptune.sentinel.zookeeper;

import com.alibaba.csp.sentinel.config.SentinelConfig;
import com.alibaba.csp.sentinel.config.SentinelConfigLoader;
import com.alibaba.csp.sentinel.heartbeat.HeartbeatSenderProvider;
import com.alibaba.csp.sentinel.transport.HeartbeatSender;
import com.alibaba.csp.sentinel.transport.config.TransportConfig;
import com.alibaba.csp.sentinel.transport.heartbeat.SimpleHttpHeartbeatSender;
import com.alibaba.csp.sentinel.util.function.Tuple2;
import com.alibaba.fastjson.JSON;
import com.neptune.sentinel.ZookeeperConfigUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.*;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 自动注册到 zookeeper
 */
@Component
@Slf4j
public class ToZookeeper implements InitializingBean {

    @Autowired
    private CuratorFramework zkClient;

    @Override
    public void afterPropertiesSet() {
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    updateAddressList();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 10, 10, TimeUnit.SECONDS);
    }

    private void updateAddressList() throws Exception {
        // 获取与控制台心跳通讯的处理类
        HeartbeatSender sender = HeartbeatSenderProvider.getHeartbeatSender();
        // 获取全部子节点
        List<String> forPathList = zkClient.getChildren().forPath(ZookeeperConfigUtil.SENTINEL_GROUP);

        // 只对 zookeeper 上有数据更新，前提是命令行的控制台配置必须维护
        if (forPathList != null) {
            log.info("获取 zookeeper 控制台地址，[{}] ", JSON.toJSON(forPathList));

            SimpleHttpHeartbeatSender heartbeatSender = null;
            // 两种通讯实现方式： SimpleHttpHeartbeatSender 或者 HttpHeartbeatSender
            if (sender instanceof SimpleHttpHeartbeatSender) {
                heartbeatSender = (SimpleHttpHeartbeatSender) sender;
            }

            // 维护地址列表，将新增的添加进去，系统配置的不删除
            Properties properties = SentinelConfigLoader.getProperties();
            for (Object key : properties.keySet()) {
                if (TransportConfig.CONSOLE_SERVER.equals(key.toString())) {
                    SentinelConfig.setConfig((String) key, (String) properties.get(key));
                    continue;
                }
            }

            if (forPathList.size() > 0) {
                String newSentinelConfig = "";
                String sentinelConfig = SentinelConfig.getConfig(TransportConfig.CONSOLE_SERVER);
                for (String forPath : forPathList) {
                    if (sentinelConfig != null) {
                        if (!sentinelConfig.matches(forPath)) {
                            sentinelConfig = sentinelConfig + "," + forPath;
                            newSentinelConfig = sentinelConfig;
                        }
                    } else {
                        newSentinelConfig = forPath + ",";
                    }
                    SentinelConfig.setConfig(TransportConfig.CONSOLE_SERVER, newSentinelConfig);
                }
                // 反射调用原生的刷新地址列表方法即可
                List<Tuple2<String, Integer>> consoleServerList = TransportConfig.getConsoleServerList();
                Field addressListField = heartbeatSender.getClass().getDeclaredField("addressList");
                addressListField.setAccessible(true);
                addressListField.set(heartbeatSender, consoleServerList);

                log.info("刷新完成与控制台列表信息:[{}]", JSON.toJSON(addressListField.get(heartbeatSender)));
            }
        }
    }
}