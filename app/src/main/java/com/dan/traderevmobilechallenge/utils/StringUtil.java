package com.dan.traderevmobilechallenge.utils;

import android.annotation.SuppressLint;
import android.content.Context;

import com.dan.traderevmobilechallenge.R;
import com.dan.traderevmobilechallenge.application.CustomApp;
import com.dan.traderevmobilechallenge.model.Photo;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * This is util class to do string-related tasks
 *
 * Created by Dan Kim on 2019-04-28
 */
public class StringUtil {

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat formatter2 = new SimpleDateFormat("E, MMM d");

    /**
     * Method to format photo info showing in full screen
     * @param photo current photo data
     * @return formatted photo info to be shown in fullscreen
     */
    public static String formatPhotoData(Photo photo) {

        Context context = CustomApp.getContext();

        String author = photo.user.name != null ? photo.user.name : context.getString(R.string.unknown);

        String createdAt = photo.createdAt != null ? photo.createdAt : context.getString(R.string.unknown);
        String formattedCreatedAt = " \u25BA";
        if (!createdAt.equals(context.getString(R.string.unknown))) {
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
        // parse description. possible to get null
        String description = photo.description != null ? photo.description : context.getString(R.string.unknown);
        // If description is null try getting alt-description. if it's also null, then showing "unknown"
        if (description.equals(context.getString(R.string.unknown))){

            if (photo.alt_description != null ){
                description = photo.alt_description;
            }
        }

        String location = photo.user.location != null ? photo.user.location : context.getString(R.string.unknown);

        return String.format(context.getString(R.string.photo_info), author,formattedCreatedAt,description,location);
    }
}
