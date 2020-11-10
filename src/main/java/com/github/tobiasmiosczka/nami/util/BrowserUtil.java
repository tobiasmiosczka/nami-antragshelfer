package com.github.tobiasmiosczka.nami.util;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class BrowserUtil {

    public static void openUrl(String url) throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI(url));
    }

}
