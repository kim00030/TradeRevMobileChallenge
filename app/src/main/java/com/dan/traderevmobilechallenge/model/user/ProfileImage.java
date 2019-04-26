package com.dan.traderevmobilechallenge.model.user;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Dan Kim on 2019-04-26
 */
public class ProfileImage {

    @SerializedName("small")
    public String small;
    @SerializedName("medium")
    public String medium;
    @SerializedName("large")
    public String large;
}
