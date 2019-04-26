package com.dan.traderevmobilechallenge.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;

import com.dan.traderevmobilechallenge.R;
import com.dan.traderevmobilechallenge.adapters.StaggeredRecyclerViewAdapter;
import com.dan.traderevmobilechallenge.databinding.ActivityMainBinding;
import com.dan.traderevmobilechallenge.model.Photo;
import com.dan.traderevmobilechallenge.viewmodel.MainActivityViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding activityMainBinding;
    private StaggeredRecyclerViewAdapter staggeredRecyclerViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initUi();
        MainActivityViewModel mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        mainActivityViewModel.getPhotosLiveData().observe(this, new Observer<List<Photo>>() {
            @Override
            public void onChanged(List<Photo> photos) {
                 staggeredRecyclerViewAdapter.setPhotos(photos);
            }
        });
    }

    private void initUi() {

        //Toolbar
        setSupportActionBar(activityMainBinding.toolBar);
        staggeredRecyclerViewAdapter = new StaggeredRecyclerViewAdapter();
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager
                (3, StaggeredGridLayoutManager.VERTICAL);
        activityMainBinding.recyclerView.setAdapter(staggeredRecyclerViewAdapter);
        activityMainBinding.recyclerView.setLayoutManager(staggeredGridLayoutManager);
        activityMainBinding.recyclerView.setHasFixedSize(true);

    }
}
