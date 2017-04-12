package com.github.tobiasmiosczka.nami.program;

import java.nio.charset.Charset;

public class FileEncodingHelper {
    public static void setFileEncoding(String fileEncoding) throws IllegalAccessException, NoSuchFieldException {
        System.setProperty("file.encoding",fileEncoding);
        java.lang.reflect.Field charset = Charset.class.getDeclaredField("defaultCharset");
        charset.setAccessible(true);
        charset.set(null,null);
    }
}
