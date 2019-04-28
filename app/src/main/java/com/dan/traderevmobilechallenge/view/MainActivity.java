package com.dan.traderevmobilechallenge.view;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.SharedElementCallback;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.dan.traderevmobilechallenge.R;
import com.dan.traderevmobilechallenge.adapters.StaggeredRecyclerViewAdapter;
import com.dan.traderevmobilechallenge.databinding.ActivityMainBinding;
import com.dan.traderevmobilechallenge.globals.Constants;
import com.dan.traderevmobilechallenge.model.Photo;
import com.dan.traderevmobilechallenge.viewmodel.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "myDebug";
    private static final int SPAN_COUNT = 2;
    private ActivityMainBinding activityMainBinding;
    private StaggeredRecyclerViewAdapter staggeredRecyclerViewAdapter;
    private MainActivityViewModel mainActivityViewModel;

    private static int currentPosition;
    private static final String KEY_CURRENT_POSITION = "com.google.samples.gridtopager.key.currentPosition";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt(KEY_CURRENT_POSITION, 0);
        }

        initUi();

        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        mainActivityViewModel.getPhotosLiveData().observe(this, new Observer<ArrayList<Photo>>() {
            @Override
            public void onChanged(ArrayList<Photo> photos) {
                staggeredRecyclerViewAdapter.setPhotos(photos);
            }
        });
    }

    private void initUi() {

        //Toolbar
        setSupportActionBar(activityMainBinding.toolBar);
        staggeredRecyclerViewAdapter = new StaggeredRecyclerViewAdapter();
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager
                (SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
        activityMainBinding.recyclerView.setAdapter(staggeredRecyclerViewAdapter);
        activityMainBinding.recyclerView.setLayoutManager(staggeredGridLayoutManager);
        activityMainBinding.recyclerView.setHasFixedSize(true);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && data.hasExtra(Constants.KEY_CURRENT_POSITION)) {
            currentPosition = data.getIntExtra(Constants.KEY_CURRENT_POSITION, 0);
            activityMainBinding.recyclerView.scrollToPosition(currentPosition-1);

        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CURRENT_POSITION, currentPosition);
    }
}
