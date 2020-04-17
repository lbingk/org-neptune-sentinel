package com.alibaba.csp.sentinel.dashboard.zookeeper;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author rodbate
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "zookeeper.config")
public class ZookeeperConfigProperties {
    private String connectString;
    private int sessionTimeout;
    private int connectionTimeout;
}
