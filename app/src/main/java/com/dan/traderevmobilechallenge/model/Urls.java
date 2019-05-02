package com.dan.traderevmobilechallenge.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Data Map for JSON object for Urls
 *
 * Created by Dan Kim on 2019-04-26
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class Urls implements Parcelable {

    @SerializedName("raw")
    public String raw;
    @SerializedName("full")
    public String full;
    @SerializedName("regular")
    public String regular;
    @SerializedName("small")
    public String small;
    @SerializedName("thumb")
    public String thumb;

    protected Urls(Parcel in) {
        raw = in.readString();
        full = in.readString();
        regular = in.readString();
        small = in.readString();
        thumb = in.readString();
    }

    public static final Creator<Urls> CREATOR = new Creator<Urls>() {
        @Override
        public Urls createFromParcel(Parcel in) {
            return new Urls(in);
        }

        @Override
        public Urls[] newArray(int size) {
            return new Urls[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(raw);
        dest.writeString(full);
        dest.writeString(regular);
        dest.writeString(small);
        dest.writeString(thumb);
    }
}
