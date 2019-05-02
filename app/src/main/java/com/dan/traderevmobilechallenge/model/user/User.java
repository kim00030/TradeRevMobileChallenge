package com.dan.traderevmobilechallenge.model.user;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Data Map for JSON object for User
 *
 * Created by Dan Kim on 2019-04-26
 */
@SuppressWarnings("ALL")
public class User implements Parcelable {

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


    protected User(Parcel in) {
        id = in.readString();
        updatedAt = in.readString();
        userName = in.readString();
        name = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        location = in.readString();
        instagramUserName = in.readString();
        totalCollections = in.readInt();
        totalLikes = in.readInt();
        totalPhotos = in.readInt();
        acceptTos = in.readByte() != 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(updatedAt);
        dest.writeString(userName);
        dest.writeString(name);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(location);
        dest.writeString(instagramUserName);
        dest.writeInt(totalCollections);
        dest.writeInt(totalLikes);
        dest.writeInt(totalPhotos);
        dest.writeByte((byte) (acceptTos ? 1 : 0));
    }
}
