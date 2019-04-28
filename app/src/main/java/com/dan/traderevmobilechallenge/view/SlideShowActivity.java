package com.dan.traderevmobilechallenge.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.SharedElementCallback;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;

import com.dan.traderevmobilechallenge.R;
import com.dan.traderevmobilechallenge.databinding.ActivitySlideShowBinding;
import com.dan.traderevmobilechallenge.globals.Constants;
import com.dan.traderevmobilechallenge.model.Photo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SlideShowActivity extends AppCompatActivity {


    private int adapterPosition;
    private ArrayList<Photo> photos;
    private ActivitySlideShowBinding activitySlideShowBinding;
    private PhotoAdapter photoAdapter;
    private int current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
        }

        activitySlideShowBinding = DataBindingUtil.setContentView(this, R.layout.activity_slide_show);

        //for animation
        //supportPostponeEnterTransition();
        Bundle extras = getIntent().getExtras();

        adapterPosition = getIntent().getIntExtra(Constants.KEY_CURRENT_POSITION, 0);
        photos = getIntent().getParcelableArrayListExtra(Constants.KEY_PHOTOS);

//        String imageTransitionName ="";
//        imageTransitionName = extras.getString(Constants.KEY_IMAGE_TRANSITION_NAME);



        photoAdapter = new PhotoAdapter(photos, this);
        activitySlideShowBinding.viewPager.setAdapter(photoAdapter);
        activitySlideShowBinding.viewPager.setCurrentItem(adapterPosition);

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent();
        intent.putExtra(Constants.KEY_CURRENT_POSITION, current);
        setResult(RESULT_OK,intent);
        finish();
        super.onBackPressed();
    }

    public class PhotoAdapter extends PagerAdapter {

        private ArrayList<Photo> photos;
        private Context context;
        private LayoutInflater layoutInflater;

        public PhotoAdapter(ArrayList<Photo> photos, Context context) {
            this.photos = photos;
            this.context = context;

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
