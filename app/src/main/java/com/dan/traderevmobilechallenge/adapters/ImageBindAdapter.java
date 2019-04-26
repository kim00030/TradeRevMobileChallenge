package com.dan.traderevmobilechallenge.adapters;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.dan.traderevmobilechallenge.R;
import com.squareup.picasso.Picasso;

/**
 *  This is Binding Adapter for binding Image into ImageView.
 *  Methods here would be called from ImageView in xml
 *
 *  Created by Dan Kim on 2019-04-26
 */
public class ImageBindAdapter {

    @BindingAdapter("photo_url")
    public static void loadPhoto(ImageView imageView, String photoUrl){

        Picasso.get()
                .load(photoUrl)
                .placeholder(R.mipmap.ic_launcher)
                .into(imageView);
    }
}
