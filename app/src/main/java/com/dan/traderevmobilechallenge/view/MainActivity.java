package com.dan.traderevmobilechallenge.view;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.SharedElementCallback;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
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

    private ArrayList<ImageView> sharedViews;
    private int exitPosition;
    private int enterPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

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
        staggeredRecyclerViewAdapter = new StaggeredRecyclerViewAdapter(sharedViewListener);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager
                (SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
        activityMainBinding.recyclerView.setAdapter(staggeredRecyclerViewAdapter);
        activityMainBinding.recyclerView.setLayoutManager(staggeredGridLayoutManager);
        activityMainBinding.recyclerView.setHasFixedSize(true);

    }

    @TargetApi(21)
    private void setCallback(final int enterPosition) {

        this.enterPosition = enterPosition;
        setExitSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {

                Log.d(TAG, "onMapSharedElements: ");


                if (exitPosition != enterPosition &&
                        names.size() > 0&& exitPosition < sharedViews.size()) {
                    names.clear();
                    sharedElements.clear();
                    View view = sharedViews.get(enterPosition);
                    names.add(view.getTransitionName());
                    sharedElements.put(view.getTransitionName(), view);

                }
                setExitSharedElementCallback((SharedElementCallback) null);
                sharedViews = null;
            }

        });
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Log.d(TAG, "onActivityReenter: ");
            exitPosition = data.getIntExtra("exit_position", enterPosition);
        }
    }

    private OnSharedViewListener sharedViewListener = new OnSharedViewListener() {
        @Override
        public void onSharedViewListener(ArrayList<ImageView> views, int enterPosition) {
            sharedViews = views;
            setCallback(enterPosition);
        }

    };

    public interface OnSharedViewListener {
        void onSharedViewListener(ArrayList<ImageView> views, int enterPosition);
    }
}
