package com.dan.traderevmobilechallenge.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.dan.traderevmobilechallenge.R;
import com.dan.traderevmobilechallenge.databinding.ActivityMainBinding;
import com.dan.traderevmobilechallenge.view.fragments.GridFragment;

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

        initUi();
    }

    private void initUi() {

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, new GridFragment(), GridFragment.class.getSimpleName())
                .commit();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CURRENT_POSITION, currentPosition);
    }
}
