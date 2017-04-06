package com.github.tobiasmiosczka.nami.program;

import java.nio.charset.Charset;

public class Helper {
    public static void setFileEncodingUTF8(){
        System.setProperty("file.encoding","UTF-8");
        try {
            java.lang.reflect.Field charset = Charset.class.getDeclaredField("defaultCharset");
            charset.setAccessible(true);
            charset.set(null,null);
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
