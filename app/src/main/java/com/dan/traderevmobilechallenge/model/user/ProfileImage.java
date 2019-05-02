package com.dan.traderevmobilechallenge.model.user;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Data Map for JSON object for Profile
 *
 * Created by Dan Kim on 2019-04-26
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class ProfileImage implements Parcelable {

    @SerializedName("small")
    public String small;
    @SerializedName("medium")
    public String medium;
    @SerializedName("large")
    public String large;

    protected ProfileImage(Parcel in) {
        small = in.readString();
        medium = in.readString();
        large = in.readString();
    }

    public static final Creator<ProfileImage> CREATOR = new Creator<ProfileImage>() {
        @Override
        public ProfileImage createFromParcel(Parcel in) {
            return new ProfileImage(in);
        }

        @Override
        public ProfileImage[] newArray(int size) {
            return new ProfileImage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(small);
        dest.writeString(medium);
        dest.writeString(large);
    }
}
