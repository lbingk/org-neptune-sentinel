package com.neptune.sentinel.zookeeper;


import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author rodbate
 */
@Slf4j
//@ConditionalOnProperty(value = "dynamic.rules.source.type", havingValue = "zookeeper")
@Configuration
@EnableConfigurationProperties(ZookeeperConfigProperties.class)
public class ZookeeperConfig {

    private static final int DEFAULT_ZK_SESSION_TIMEOUT = 30000;
    private static final int DEFAULT_ZK_CONNECTION_TIMEOUT = 10000;
    private static final int RETRY_TIMES = 3;
    private static final int SLEEP_TIME = 1000;

    public ZookeeperConfig() {
        log.info("============== Use Zookeeper Dynamic Rules Source ===================");
    }


    /**
     * zk client
     *
     * @param properties zk properties
     * @return zk client
     */
    @Bean(destroyMethod = "close")
    public CuratorFramework zkClient(ZookeeperConfigProperties properties) {
        String connectString = properties.getConnectString();
        int sessionTimeout = DEFAULT_ZK_SESSION_TIMEOUT;
        int connectionTimeout = DEFAULT_ZK_CONNECTION_TIMEOUT;
        if (properties.getSessionTimeout() > 0) {
            sessionTimeout = properties.getSessionTimeout();
        }
        if (properties.getConnectionTimeout() > 0) {
            connectionTimeout = properties.getConnectionTimeout();
        }

        CuratorFramework zkClient = CuratorFrameworkFactory.newClient(connectString, sessionTimeout, connectionTimeout,
                new ExponentialBackoffRetry(SLEEP_TIME, RETRY_TIMES));
        zkClient.start();

        log.info("Initialize zk client CuratorFramework, connectString={}, sessionTimeout={}, connectionTimeout={}, retry=[sleepTime={}, retryTime={}]",
                connectString, sessionTimeout, connectionTimeout, SLEEP_TIME, RETRY_TIMES);
        return zkClient;
    }
}
