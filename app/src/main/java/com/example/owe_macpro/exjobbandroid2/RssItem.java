package com.example.owe_macpro.exjobbandroid2;

/**
 * Created by owe-macpro on 06/04/16.
 */
public class RssItem {

    private String title;
    private String link;
    private String description;
    private String enclosure_url;
    private String feed_title;

    public String getFeed_title() {
        return feed_title;
    }

    public void setFeed_title(String feed_title) {
        this.feed_title = feed_title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEnclosure_url() {
        return enclosure_url;
    }

    public void setEnclosure_url(String enclosure_url) {
        this.enclosure_url = enclosure_url;
    }

    @Override
    public String toString() {
        return title + " - " + link + "\n" + description + "-" + enclosure_url
                + "\n";
    }

}
