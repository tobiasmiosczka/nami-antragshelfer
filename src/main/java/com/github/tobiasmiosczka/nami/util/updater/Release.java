
package com.github.tobiasmiosczka.nami.util.updater;

import com.google.gson.annotations.SerializedName;

public class Release {

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
