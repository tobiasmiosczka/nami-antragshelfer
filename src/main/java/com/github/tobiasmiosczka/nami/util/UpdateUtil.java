package com.github.tobiasmiosczka.nami.util;

import com.github.tobiasmiosczka.nami.util.updater.Release;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

public class UpdateUtil {

    private static final Gson GSON = new GsonBuilder().create();

    private static Release getLastestRelease() throws IOException {
        URL url = new URL("https://api.github.com/repos/tobiasmiosczka/nami-antragshelfer/releases/latest");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        return GSON.fromJson(new InputStreamReader(con.getInputStream()), Release.class);
    }

    public static boolean updateAvailable() throws IOException {
        return !getLastestRelease().getTagName().equals(VersionUtil.VERSION);
    }

    public static void downloadUpdate() throws IOException, URISyntaxException {
        BrowserUtil.openUrl(getLastestRelease().getHtmlUrl());
    }

}
