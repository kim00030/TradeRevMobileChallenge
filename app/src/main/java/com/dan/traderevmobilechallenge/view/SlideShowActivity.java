package com.dan.traderevmobilechallenge.view;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.dan.traderevmobilechallenge.R;
import com.dan.traderevmobilechallenge.databinding.ActivitySlideShowBinding;
import com.dan.traderevmobilechallenge.globals.Constants;
import com.dan.traderevmobilechallenge.model.Photo;
import com.dan.traderevmobilechallenge.utils.StringUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SlideShowActivity extends AppCompatActivity {

    private static final String TAG = "SlideShowActivity";
    public static final int REQ_START_SLIDE_PHOTO = 100;
    private int current;
    private ActivitySlideShowBinding activitySlideShowBinding;

    private boolean toggleClick = false;
    private ArrayList<Photo> photos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
        }
        activitySlideShowBinding = DataBindingUtil.setContentView(this, R.layout.activity_slide_show);

        int adapterPosition = getIntent().getIntExtra(Constants.KEY_CURRENT_POSITION, 0);

        photos = getIntent().getParcelableArrayListExtra(Constants.KEY_PHOTOS);

        PhotoAdapter photoAdapter = new PhotoAdapter(photos, this);
        activitySlideShowBinding.viewPager.setAdapter(photoAdapter);
        activitySlideShowBinding.viewPager.setCurrentItem(adapterPosition);

        initFloatButton();
        addViewPagerPageListener();

    }

    private void addViewPagerPageListener() {

        activitySlideShowBinding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                runOnUiThread(() -> {
                    // set photo info showing in full screen page
                    String photoInfo = StringUtil.formatPhotoData(photos.get(position));
                    activitySlideShowBinding.tvPhotoInfo.setText(photoInfo);
                });
            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected: ");

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d(TAG, "onPageScrollStateChanged: ");
            }
        });
    }

    private void initFloatButton() {

        activitySlideShowBinding.fbBtn.setImageResource(R.drawable.eye_off_outline);

        //Animation with TextField for showing photo info
        runOnUiThread(()-> YoYo.with(Techniques.Tada)
                .duration(700)
                .repeat(1)
                .playOn(activitySlideShowBinding.tvPhotoInfo));

        activitySlideShowBinding.fbBtn.setOnClickListener(v -> runOnUiThread(() -> {
            toggleClick = !toggleClick;
            if (toggleClick) {

                activitySlideShowBinding.fbBtn.setImageResource(R.drawable.eye_outline);
                activitySlideShowBinding.tvPhotoInfo.setVisibility(View.INVISIBLE);
            } else {
                activitySlideShowBinding.tvPhotoInfo.setVisibility(View.VISIBLE);
                activitySlideShowBinding.fbBtn.setImageResource(R.drawable.eye_off_outline);
            }
        }));
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