package com.codingrippler.template_service.config;

import java.util.HashMap;
import java.util.Map;

public class ConfigurationCache {

    private static Map<String, String> config = new HashMap<>();;

    public static void setConfig(Map<String, String> config) {
        ConfigurationCache.config = config;
    }

    public static String getConfigValue(String key) {
        return config.get(key);
    }

}
