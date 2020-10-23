package com.github.tobiasmiosczka.nami.util;

import java.lang.reflect.Field;
import java.nio.charset.Charset;

public class FileEncodingUtil {
    public static void setFileEncoding(String fileEncoding) throws IllegalAccessException, NoSuchFieldException {
        System.setProperty("file.encoding",fileEncoding);
        Field charset = Charset.class.getDeclaredField("defaultCharset");
        charset.setAccessible(true);
        charset.set(null,null);
    }
}
