package com.zes.squad.gmh.web.property;

import java.io.IOException;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExportProperties {

    private static Properties properties;

    static {
        properties = new Properties();
        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("export.properties"));
        } catch (IOException e) {
            log.error("读取export.properties文件异常", e);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}
