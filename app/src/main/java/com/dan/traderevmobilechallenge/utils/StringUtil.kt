package com.dan.traderevmobilechallenge.utils

import android.annotation.SuppressLint
import android.content.Context

import com.dan.traderevmobilechallenge.R
import com.dan.traderevmobilechallenge.application.CustomApp
import com.dan.traderevmobilechallenge.model.Photo

import java.text.ParseException
import java.text.SimpleDateFormat

/**
 * This is util class to do string-related tasks
 *
 * Created by Dan Kim on 2019-04-28
 */
object StringUtil {

    @SuppressLint("SimpleDateFormat")
    private val formatter1 = SimpleDateFormat("yyyy-MM-dd")
    @SuppressLint("SimpleDateFormat")
    private val formatter2 = SimpleDateFormat("E, MMM d")

    /**
     * Method to format photo info showing in full screen
     * @param photo current photo data
     * @return formatted photo info to be shown in fullscreen
     */
    fun formatPhotoData(photo: Photo): String {

        val context = CustomApp.getContext()

        val author = if (photo.user.name != null) photo.user.name else context.getString(R.string.unknown)

        val createdAt = if (photo.createdAt != null) photo.createdAt else context.getString(R.string.unknown)
        var formattedCreatedAt = " \u25BA"
        if (createdAt != context.getString(R.string.unknown)) {
            val temp = createdAt.split("T".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            try {
                formattedCreatedAt += formatter2.format(formatter1.parse(temp[0]))
            } catch (e: ParseException) {
                // If formatting occurs , just return parameter
                formattedCreatedAt += createdAt
            }

        } else {
            formattedCreatedAt = createdAt
        }
        // parse description. possible to get null
        var description = if (photo.description != null) photo.description else context.getString(R.string.unknown)
        // If description is null try getting alt-description. if it's also null, then showing "unknown"
        if (description == context.getString(R.string.unknown)) {

            if (photo.alt_description != null) {
                description = photo.alt_description
            }
        }

        val location = if (photo.user.location != null) photo.user.location else context.getString(R.string.unknown)

        return String.format(context.getString(R.string.photo_info), author, formattedCreatedAt, description, location)
    }
}
