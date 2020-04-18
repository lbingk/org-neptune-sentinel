/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.csp.sentinel.dashboard.rule.zookeeper;


import com.neptune.sentinel.RuleType;
import org.apache.commons.lang.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ZookeeperConfigUtil {
    public static final String SENTINEL_GROUP = "/SENTINEL-DASHBOARD_APPLICATION";
    public static final String RULE_ROOT_PATH = "/SENTINEL_RULE_CONFIG";
    public static final String AUTHORITY_ROOT_PATH = "/SENTINEL_AUTHORITY_CONFIG";
    public static final String HOT_ROOT_PATH = "/SENTINEL_HOT_CONFIG";
    public static final String SYSTEM_ROOT_PATH = "/SENTINEL_SYSTEM_CONFIG";
    public static final String DEGRADE_ROOT_PATH = "/SENTINEL_DEGRADE_CONFIG";
    public static final String FOW_ROOT_PATH = "/SENTINEL_FLOW_CONFIG";

    public static final int RETRY_TIMES = 3;
    public static final int SLEEP_TIME = 1000;
    public static final String SEPARATE = "/";
    public static final String MACHINE_ADDRESS = "machine_address";

    public static String getLocalIp() throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        return inetAddress.getHostAddress();
    }

    public static String getLocalPort() {
        return "8081";
    }

    public static String getPath(String appName, RuleType ruleType) {
        StringBuilder stringBuilder = new StringBuilder(RULE_ROOT_PATH);
        String nodeName = "";
        switch (ruleType) {
            case AUTHORITY:
                nodeName = AUTHORITY_ROOT_PATH;
                break;
            case HOT:
                nodeName = HOT_ROOT_PATH;
                break;
            case SYSTEM:
                nodeName = SYSTEM_ROOT_PATH;
                break;
            case DEGRADE:
                nodeName = DEGRADE_ROOT_PATH;
                break;
            case FLOW:
                nodeName = FOW_ROOT_PATH;
                break;
        }

        if (StringUtils.isBlank(appName)) {
            return stringBuilder.toString();
        }
        if (appName.startsWith("/")) {
            stringBuilder.append(appName);
        } else {
            stringBuilder.append("/")
                    .append(appName);
        }

        stringBuilder.append(nodeName);
        return stringBuilder.toString();
    }
}