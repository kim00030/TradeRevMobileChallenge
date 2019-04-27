package com.dan.traderevmobilechallenge.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
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

public class MainActivity extends AppCompatActivity implements StaggeredRecyclerViewAdapter.Callback {

    private static final int SPAN_COUNT = 2;
    private ActivityMainBinding activityMainBinding;
    private StaggeredRecyclerViewAdapter staggeredRecyclerViewAdapter;
    private MainActivityViewModel mainActivityViewModel;

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
        staggeredRecyclerViewAdapter = new StaggeredRecyclerViewAdapter(this);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager
                (SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
        activityMainBinding.recyclerView.setAdapter(staggeredRecyclerViewAdapter);
        activityMainBinding.recyclerView.setLayoutManager(staggeredGridLayoutManager);
        activityMainBinding.recyclerView.setHasFixedSize(true);

    }

    @Override
    public void onClickItem(int position) {

        Intent intent = new Intent(this, SlideShowActivity.class);
        intent.putExtra(Constants.KEY_CURRENT_POSITION, position);
        intent.putParcelableArrayListExtra(Constants.KEY_PHOTOS, mainActivityViewModel.getPhotosLiveData().getValue());
        startActivity(intent);
    }
}
