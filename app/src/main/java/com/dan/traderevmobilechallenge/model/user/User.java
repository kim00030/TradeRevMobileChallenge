package com.dan.traderevmobilechallenge.model.user;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Dan Kim on 2019-04-26
 */
public class User {

    @SerializedName("id")
    public String id;
    @SerializedName("updated_at")
    public String updatedAt;
    @SerializedName("username")
    public String userName;
    @SerializedName("name")
    public String name;
    @SerializedName("first_name")
    public String firstName;
    @SerializedName("last_name")
    public String lastName;
    @SerializedName("location")
    public String location;
    @SerializedName("profile_image")
    public ProfileImage profileImage;
    @SerializedName("instagram_username")
    public String instagramUserName;
    @SerializedName("totalCollections")
    int totalCollections;
    @SerializedName("total_likes")
    int totalLikes;
    @SerializedName("total_photos")
    int totalPhotos;
    @SerializedName("accepted_tos")
    boolean acceptTos;


}
