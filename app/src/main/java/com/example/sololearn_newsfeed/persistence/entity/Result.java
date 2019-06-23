package com.example.sololearn_newsfeed.persistence.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.example.sololearn_newsfeed.network.Thumbnails;
import com.example.sololearn_newsfeed.utils.ThumbnailsConverter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class Result implements Parcelable {

    public static final Creator<Result> CREATOR = new Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };
    @PrimaryKey
    @NonNull
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("sectionId")
    @Expose
    private String sectionId;
    @SerializedName("sectionName")
    @Expose
    private String sectionName;
    @SerializedName("webPublicationDate")
    @Expose
    private String webPublicationDate;
    @SerializedName("webTitle")
    @Expose
    private String webTitle;
    @SerializedName("webUrl")
    @Expose
    private String webUrl;
    @SerializedName("apiUrl")
    @Expose
    private String apiUrl;
    @SerializedName("isHosted")
    @Expose
    private Boolean isHosted;
    @SerializedName("pillarId")
    @Expose
    private String pillarId;
    @SerializedName("pillarName")
    @Expose
    private String pillarName;
    @SerializedName("fields")
    @TypeConverters(ThumbnailsConverter.class)
    @Expose
    private Thumbnails thumbnail;

    protected Result(Parcel in) {
        id = in.readString();
        type = in.readString();
        sectionId = in.readString();
        sectionName = in.readString();
        webPublicationDate = in.readString();
        webTitle = in.readString();
        webUrl = in.readString();
        apiUrl = in.readString();
        byte tmpIsHosted = in.readByte();
        isHosted = tmpIsHosted == 0 ? null : tmpIsHosted == 1;
        pillarId = in.readString();
        pillarName = in.readString();

    }

    public Result() {
    }

    public Thumbnails getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnails thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Result withId(String id) {
        this.id = id;
        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Result withType(String type) {
        this.type = type;
        return this;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public Result withSectionId(String sectionId) {
        this.sectionId = sectionId;
        return this;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public Result withSectionName(String sectionName) {
        this.sectionName = sectionName;
        return this;
    }

    public String getWebPublicationDate() {
        return webPublicationDate;
    }

    public void setWebPublicationDate(String webPublicationDate) {
        this.webPublicationDate = webPublicationDate;
    }

    public Result withWebPublicationDate(String webPublicationDate) {
        this.webPublicationDate = webPublicationDate;
        return this;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public void setWebTitle(String webTitle) {
        this.webTitle = webTitle;
    }

    public Result withWebTitle(String webTitle) {
        this.webTitle = webTitle;
        return this;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public Result withWebUrl(String webUrl) {
        this.webUrl = webUrl;
        return this;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public Result withApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
        return this;
    }


    public Boolean getIsHosted() {
        return isHosted;
    }

    public void setIsHosted(Boolean isHosted) {
        this.isHosted = isHosted;
    }

    public Result withIsHosted(Boolean isHosted) {
        this.isHosted = isHosted;
        return this;
    }

    public String getPillarId() {
        return pillarId;
    }

    public void setPillarId(String pillarId) {
        this.pillarId = pillarId;
    }

    public Result withPillarId(String pillarId) {
        this.pillarId = pillarId;
        return this;
    }

    public String getPillarName() {
        return pillarName;
    }

    public void setPillarName(String pillarName) {
        this.pillarName = pillarName;
    }

    public Result withPillarName(String pillarName) {
        this.pillarName = pillarName;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(type);
        dest.writeString(sectionId);
        dest.writeString(sectionName);
        dest.writeString(webPublicationDate);
        dest.writeString(webTitle);
        dest.writeString(webUrl);
        dest.writeString(apiUrl);
        dest.writeByte((byte) (isHosted == null ? 0 : isHosted ? 1 : 2));
        dest.writeString(pillarId);
        dest.writeString(pillarName);
    }
}