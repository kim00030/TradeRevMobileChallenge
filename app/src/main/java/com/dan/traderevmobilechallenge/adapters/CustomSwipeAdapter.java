package com.dan.traderevmobilechallenge.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.dan.traderevmobilechallenge.R;
import com.dan.traderevmobilechallenge.model.Photo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Dan Kim on 2019-04-27
 */
public class CustomSwipeAdapter extends PagerAdapter {

    private ArrayList<Photo> photos;
    private Context context;
    private LayoutInflater layoutInflater;


    public CustomSwipeAdapter(Context context, ArrayList<Photo> photos) {
        this.context = context;
        this.photos = photos;
    }

    @Override
    public int getCount() {
        return photos == null ? 0 : photos.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        if (layoutInflater == null) {
            layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        View view = layoutInflater.inflate(R.layout.image_fullscreen_preview, container, false);

        ImageView imageView = view.findViewById(R.id.imageView);

        Photo photo = this.photos.get(position);

        Picasso.get().load(photo.urls.regular).into(imageView);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
