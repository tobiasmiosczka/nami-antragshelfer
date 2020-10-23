package com.github.tobiasmiosczka.nami.util;

import java.util.Properties;

public class VersionUtil {

    public static final String VERSION = getVersion();

    private static String getVersion() {
        Properties props = new Properties();
        String v;
        try {
            props.load(ClassLoader.getSystemResourceAsStream("META-INF/maven/com.github.tobiasmiosczka/nami-antragshelfer/pom.properties"));
            v = props.getProperty("version");
        } catch (Exception e) {
           v = "UNKNOWN VERSION";
        }
        return v;
    }
}
