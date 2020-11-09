package com.github.tobiasmiosczka.nami.updater;

import com.github.tobiasmiosczka.nami.util.BrowserUtil;
import com.github.tobiasmiosczka.nami.util.VersionUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

public class Updater {

    private static class Release {

        @SerializedName("html_url")
        private String htmlUrl;
        @SerializedName("tag_name")
        private String tagName;
        private String name;
        private Boolean draft;
        private Boolean prerelease;

        public String getHtmlUrl() {
            return htmlUrl;
        }

        public String getTagName() {
            return tagName;
        }

        public String getName() {
            return name;
        }

        public Boolean isDraft() {
            return draft;
        }

        public Boolean isPrerelease() {
            return prerelease;
        }

    }

    private static final Gson GSON = new GsonBuilder().create();

    private static Release getLatestRelease() throws IOException {
        URL url = new URL("https://api.github.com/repos/tobiasmiosczka/nami-antragshelfer/releases/latest");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        return GSON.fromJson(new InputStreamReader(con.getInputStream()), Release.class);
    }

    public static boolean updateAvailable() throws IOException {
        return !getLatestRelease().getTagName().equals(VersionUtil.VERSION);
    }

    public static void downloadUpdate() throws IOException, URISyntaxException {
        BrowserUtil.openUrl(getLatestRelease().getHtmlUrl());
    }

}
