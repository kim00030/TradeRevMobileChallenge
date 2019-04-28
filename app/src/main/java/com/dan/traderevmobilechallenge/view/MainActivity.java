package com.dan.traderevmobilechallenge.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.dan.traderevmobilechallenge.R;
import com.dan.traderevmobilechallenge.databinding.ActivityMainBinding;
import com.dan.traderevmobilechallenge.globals.Constants;
import com.dan.traderevmobilechallenge.view.fragments.GridViewFragment;

public class MainActivity extends AppCompatActivity {

    public static int currentPosition;
    private static final String KEY_CURRENT_POSITION = "key_current_position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        //Toolbar
        setSupportActionBar(activityMainBinding.toolBar);
        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt(KEY_CURRENT_POSITION, 0);
            return;
        }

        loadGridViewFragment();
    }

    private void loadGridViewFragment() {

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, new GridViewFragment(), GridViewFragment.class.getSimpleName())
                .commit();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CURRENT_POSITION, currentPosition);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SlideShowActivity.REQ_START_SLIDE_PHOTO && resultCode == Activity.RESULT_OK) {
            currentPosition = data != null ? data.getIntExtra(Constants.KEY_CURRENT_POSITION, 0) : 0;
            GridViewFragment fragment = (GridViewFragment) getSupportFragmentManager().findFragmentByTag(GridViewFragment.class.getSimpleName());
            if (fragment != null) {
                fragment.updateCurrentPosition(currentPosition);
            }
        }
    }
}
