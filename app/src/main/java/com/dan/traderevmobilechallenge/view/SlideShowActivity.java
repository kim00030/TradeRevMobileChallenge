package com.dan.traderevmobilechallenge.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.dan.traderevmobilechallenge.R;
import com.dan.traderevmobilechallenge.adapters.CustomSwipeAdapter;
import com.dan.traderevmobilechallenge.databinding.ActivitySlideShowBinding;
import com.dan.traderevmobilechallenge.globals.Constants;
import com.dan.traderevmobilechallenge.model.Photo;

import java.util.ArrayList;

public class SlideShowActivity extends AppCompatActivity {


    private int selectedPosition;
    private ArrayList<Photo> photos;
    private ActivitySlideShowBinding activitySlideShowBinding;
    private CustomSwipeAdapter customSwipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activitySlideShowBinding = DataBindingUtil.setContentView(this, R.layout.activity_slide_show);

        selectedPosition = getIntent().getIntExtra(Constants.KEY_CURRENT_POSITION, 0);
        photos = getIntent().getParcelableArrayListExtra(Constants.KEY_PHOTOS);
        customSwipeAdapter = new CustomSwipeAdapter(this, photos);
        activitySlideShowBinding.viewPager.setAdapter(customSwipeAdapter);
        activitySlideShowBinding.viewPager.setCurrentItem(selectedPosition, false);

    }
}
