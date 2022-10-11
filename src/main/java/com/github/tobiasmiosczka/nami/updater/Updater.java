package com.github.tobiasmiosczka.nami.updater;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tobiasmiosczka.nami.util.BrowserUtil;
import com.github.tobiasmiosczka.nami.util.VersionUtil;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

public class Updater {

    private static class Release {

        @JsonProperty("html_url")
        private String htmlUrl;
        @JsonProperty("tag_name")
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

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private static Release getLatestRelease() throws IOException {
        URL url = new URL("https://api.github.com/repos/tobiasmiosczka/nami-antragshelfer/releases/latest");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        return OBJECT_MAPPER.readValue(new InputStreamReader(con.getInputStream()), Release.class);
    }

    public static boolean updateAvailable() throws IOException {
        return !getLatestRelease().getTagName().equals(VersionUtil.VERSION);
    }

    public static void downloadUpdate() throws IOException, URISyntaxException {
        BrowserUtil.openUrl(getLatestRelease().getHtmlUrl());
    }

}
