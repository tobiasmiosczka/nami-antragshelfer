package com.github.tobiasmiosczka.nami.util;

import java.util.Properties;

public class VersionUtil {

    private static final String PATH = "META-INF/maven/com.github.tobiasmiosczka/nami-antragshelfer/pom.properties";
    public static final String VERSION = getVersion();

    private static String getVersion() {
        try {
            Properties props = new Properties();
            props.load(ClassLoader.getSystemResourceAsStream(PATH));
            return props.getProperty("version");
        } catch (Exception e) {
            return  "UNKNOWN VERSION";
        }
    }
}
