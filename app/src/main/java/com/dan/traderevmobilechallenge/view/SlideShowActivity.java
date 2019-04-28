package com.dan.traderevmobilechallenge.view;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;

import com.dan.traderevmobilechallenge.R;
import com.dan.traderevmobilechallenge.databinding.ActivitySlideShowBinding;
import com.dan.traderevmobilechallenge.globals.Constants;
import com.dan.traderevmobilechallenge.model.Photo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SlideShowActivity extends AppCompatActivity {

    public static final int REQ_START_SLIDE_PHOTO = 100;
    private int current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
        }

        ActivitySlideShowBinding activitySlideShowBinding = DataBindingUtil.setContentView(this, R.layout.activity_slide_show);

        int adapterPosition = getIntent().getIntExtra(Constants.KEY_CURRENT_POSITION, 0);
        ArrayList<Photo> photos = getIntent().getParcelableArrayListExtra(Constants.KEY_PHOTOS);

        PhotoAdapter photoAdapter = new PhotoAdapter(photos, this);
        activitySlideShowBinding.viewPager.setAdapter(photoAdapter);
        activitySlideShowBinding.viewPager.setCurrentItem(adapterPosition);

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent();
        intent.putExtra(Constants.KEY_CURRENT_POSITION, current);
        setResult(RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }

    class PhotoAdapter extends PagerAdapter {

        private final ArrayList<Photo> photos;
        private final Context context;
        private LayoutInflater layoutInflater;

        PhotoAdapter(ArrayList<Photo> photos, Context context) {
            this.photos = photos;
            this.context = context;

        }

        @Override
        public int getCount() {
            return photos == null ? 0 : photos.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
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
            current = position;

            Picasso.get().load(photo.urls.regular).into(imageView);

            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}