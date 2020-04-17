package com.alibaba.csp.sentinel.dashboard.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    public void afterPropertiesSet() throws Exception {

        String ipPort = ZookeeperConfigUtil.getLocalIp() + ":" + ZookeeperConfigUtil.getLocalPort();

        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                String path = ZookeeperConfigUtil.SENTINEL_GROUP + ZookeeperConfigUtil.SEPARATE + ipPort;
                try {
                    if (zkClient.checkExists().forPath(path) == null) {
                        // 父节点不存在的时候则自动创建，父节点为持久节点，子节点为指定类型
                        zkClient.create().creatingParentContainersIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
                        log.info("注册控制台信息到zookeeper：[{}]", ipPort);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 5, 5, TimeUnit.SECONDS);
    }
}
