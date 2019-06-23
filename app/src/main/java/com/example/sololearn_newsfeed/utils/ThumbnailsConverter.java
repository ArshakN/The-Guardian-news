package com.example.sololearn_newsfeed.utils;

import android.arch.persistence.room.TypeConverter;

import com.example.sololearn_newsfeed.network.Thumbnails;

public class ThumbnailsConverter {

    @TypeConverter
    public String fromThumbnail(Thumbnails thumbnails) {
        return thumbnails.toString();
    }

    @TypeConverter
    public Thumbnails toThumbnails(String data) {
        Thumbnails thumbnails = new Thumbnails(data);
        thumbnails.setThumbnail(data);
        return thumbnails;
    }

}
