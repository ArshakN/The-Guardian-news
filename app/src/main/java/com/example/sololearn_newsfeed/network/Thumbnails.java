package com.example.sololearn_newsfeed.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Thumbnails {
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;

    public Thumbnails(String str) {
        thumbnail = str;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public String toString() {
        return thumbnail;
    }
}
