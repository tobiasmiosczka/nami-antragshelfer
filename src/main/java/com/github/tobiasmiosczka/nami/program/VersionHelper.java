package com.github.tobiasmiosczka.nami.program;

import java.io.IOException;
import java.util.Properties;

public class VersionHelper {

    public static final String VERSION = getVersion();

    private static String getVersion() {
        Properties props = new Properties();
        try
        {
            props.load(ClassLoader.getSystemResourceAsStream("META-INF/maven/com.github.tobiasmiosczka/nami-antragshelfer/pom.properties"));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return props.getProperty("version");
    }
}
