package com.dan.traderevmobilechallenge.utils;

import android.annotation.SuppressLint;
import android.content.Context;

import com.dan.traderevmobilechallenge.R;
import com.dan.traderevmobilechallenge.application.CustomApp;
import com.dan.traderevmobilechallenge.model.Photo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;

/**
 * Created by Dan Kim on 2019-04-28
 */
public class StringUtil {

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat formatter2 = new SimpleDateFormat("E, MMM d");

    public static String formatPhotoData(Photo photo) {

        Context context = CustomApp.getContext();

        String author = photo.user.name != null ? photo.user.name : "Unknown";

        String createdAt = photo.createdAt != null ? photo.createdAt : "Unknown";
        String formattedCreatedAt = " \u25BA";
        if (!createdAt.equals("Unknown")) {
            String[] temp = createdAt.split("T");
            try {
                formattedCreatedAt += formatter2.format(formatter1.parse(temp[0]));
            } catch (ParseException e) {
                // If formatting occurs , just return parameter
                formattedCreatedAt += createdAt;
            }
        } else {
            formattedCreatedAt = createdAt;
        }

        String description = photo.description != null ? photo.description : "Unknown";
        if (Objects.equals(description, "Unknown") && photo.altDescription != null) {
            description = photo.altDescription;
        } else {
            description = "Unknown";
        }
        String location = photo.user.location != null ? photo.user.location : "Unknown";

        return String.format(context.getString(R.string.photo_info), author,formattedCreatedAt,description,location);
    }
}
