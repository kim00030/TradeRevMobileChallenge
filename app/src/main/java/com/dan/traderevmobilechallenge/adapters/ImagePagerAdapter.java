package com.dan.traderevmobilechallenge.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.dan.traderevmobilechallenge.model.Photo;
import com.dan.traderevmobilechallenge.view.fragments.ImageFragment;

import java.util.ArrayList;

/**
 *
 * Adapter to handle swapping photos in full screen
 * {@link com.dan.traderevmobilechallenge.view.fragments.FullImagePagerFragment}
 * Created by Dan Kim on 2019-04-30
 */
public class ImagePagerAdapter extends FragmentStatePagerAdapter {
    private final ArrayList<Photo> photos;

    public ImagePagerAdapter(Fragment fragment, ArrayList<Photo> photos) {
        super(fragment.getChildFragmentManager());
        this.photos = photos;
    }

    @Override
    public Fragment getItem(int position) {
        return ImageFragment.newInstance(photos.get(position));
    }

    @Override
    public int getCount() {
        return photos.size();
    }
}
