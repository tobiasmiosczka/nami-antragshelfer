package com.github.tobiasmiosczka.nami.util;

import java.util.Properties;

public class VersionUtil {

    private static final String PATH = "META-INF/maven/com.github.tobiasmiosczka/nami-antragshelfer/pom.properties";
    public static final String VERSION;

    static  {
        Properties props = new Properties();
        String v;
        try {
            props.load(ClassLoader.getSystemResourceAsStream(PATH));
            v = props.getProperty("version");
        } catch (Exception e) {
            v = "UNKNOWN VERSION";
        }
        VERSION = v;
    }
}
