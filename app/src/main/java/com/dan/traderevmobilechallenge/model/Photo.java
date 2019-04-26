package com.dan.traderevmobilechallenge.model;

import com.dan.traderevmobilechallenge.model.user.User;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Dan Kim on 2019-04-26
 */
public class Photo {

    @SerializedName("id")
    public String id;
    @SerializedName("created_at")
    public String CreatedAt;
    @SerializedName("updated_at")
    public String updatedAt;
    @SerializedName("width")
    public int width;
    @SerializedName("height")
    public int height;
    @SerializedName("color")
    public String color;
    @SerializedName("description")
    public String description;
    @SerializedName("alt_description")
    public String altDescription;
    @SerializedName("urls")
    public Urls urls;
    @SerializedName("user")
    public User user;



}
