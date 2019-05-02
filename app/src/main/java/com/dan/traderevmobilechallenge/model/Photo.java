package com.dan.traderevmobilechallenge.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.dan.traderevmobilechallenge.model.user.User;
import com.google.gson.annotations.SerializedName;

/**
 * This Data map class for photo info received from server
 *
 * Created by Dan Kim on 2019-04-26
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class Photo implements Parcelable {

    @SerializedName("id")
    public String id;
    @SerializedName("created_at")
    public String createdAt;
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
    public String alt_description;
    @SerializedName("urls")
    public Urls urls;
    @SerializedName("user")
    public User user;

    protected Photo(Parcel in) {
        id = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        width = in.readInt();
        height = in.readInt();
        color = in.readString();
        description = in.readString();
        alt_description = in.readString();
        urls = in.readParcelable(Urls.class.getClassLoader());
        user = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeInt(width);
        dest.writeInt(height);
        dest.writeString(color);
        dest.writeString(description);
        dest.writeString(alt_description);
        dest.writeParcelable(urls, flags);
        dest.writeParcelable(user, flags);
    }
}
