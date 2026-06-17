package com.company.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import lombok.Getter;

/** Конфигурация */
@Getter
public class Config {
    private static final Properties props = new Properties();

    static {
        try (InputStream is =
                Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            props.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при загрузке конфига из файла config.properties", e);
        }
    }

    // WordPress API
    public static String getWpBaseUrl() {
        return props.getProperty("wp.api.url");
    }

    public static String getWpPrefix() {
        return "/index.php?rest_route=/wp/v2";
    }

    public static String getWpUser() {
        return props.getProperty("wp.api.user");
    }

    public static String getWpPass() {
        return props.getProperty("wp.api.pass");
    }

    // DB
    public static String getDbUrl() {
        return "jdbc:mysql://"
                + props.getProperty("db.host")
                + ":"
                + props.getProperty("db.port")
                + "/"
                + props.getProperty("db.name");
    }

    public static String getDbUser() {
        return props.getProperty("db.user");
    }

    public static String getDbPass() {
        return props.getProperty("db.pass");
    }
}
