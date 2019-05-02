package com.dan.traderevmobilechallenge.view.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dan.traderevmobilechallenge.R;
import com.dan.traderevmobilechallenge.application.CustomApp;
import com.dan.traderevmobilechallenge.model.Photo;

/**
 *  A Fragment that instantiates a full screen page showing each photo
 *  Calls from {@link com.dan.traderevmobilechallenge.adapters.ImagePagerAdapter}
 */
@SuppressWarnings("ALL")
public class ImageFragment extends Fragment {

    private static final String KEY_PHOTO = "com.dan.traderevmobilechallenge.view.fragments.key.photo";

    public static ImageFragment newInstance(Photo photo) {
        ImageFragment fragment = new ImageFragment();
        Bundle argument = new Bundle();
        argument.putParcelable(KEY_PHOTO, photo);
        fragment.setArguments(argument);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_image, container, false);
        // Parse data sent from ImagePagerAdapter
        Bundle arguments = getArguments();
        Photo photo = arguments.getParcelable(KEY_PHOTO);
        // Set transition name set in GridAdapter
        view.findViewById(R.id.image).setTransitionName(CustomApp.getContext().getString(R.string.photo) + photo.id);
        // load photo
        Glide.with(this)
                .load(photo.urls.regular)
                .listener(new RequestListener<Drawable>() {
                    @SuppressWarnings("ConstantConditions")
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        getParentFragment().startPostponedEnterTransition();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        getParentFragment().startPostponedEnterTransition();
                        return false;
                    }
                }).into((ImageView) view.findViewById(R.id.image));

        return view;
    }
}
