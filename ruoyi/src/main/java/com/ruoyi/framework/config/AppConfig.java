package com.ruoyi.framework.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取手机app相关配置
 *
 * @author ruoyi
 */
@Component
@ConfigurationProperties(prefix = "app")
public class AppConfig
{
    /** 手机app  appId */
    private static String appId;

    /** 手机app  appKey */
    private static String appKey;


    public static String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        AppConfig.appId = appId;
    }

    public static String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        AppConfig.appKey = appKey;
    }
}
