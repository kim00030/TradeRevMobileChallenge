package com.dan.traderevmobilechallenge.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.dan.traderevmobilechallenge.R;
import com.dan.traderevmobilechallenge.databinding.ActivityMainBinding;
import com.dan.traderevmobilechallenge.view.fragments.GridViewFragment;
import com.dan.traderevmobilechallenge.viewmodel.MainActivityViewModel;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {

    public static int currentPosition;
    private static final String KEY_CURRENT_POSITION = "key_current_position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Data binding object created by compiler
        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //ViewModel
        MainActivityViewModel mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        mainActivityViewModel.getNetworkStateLiveData().observe(this, newWorkOk -> {
            // Network problem occurs
            if (!newWorkOk){
                Toasty.error(MainActivity.this,getString(R.string.no_internet), Toasty.LENGTH_SHORT).show();
            }

        });

        //Toolbar
        setSupportActionBar(activityMainBinding.toolBar);
        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt(KEY_CURRENT_POSITION, 0);
            return;
        }

        loadGridViewFragment();
    }

    /**
     * Method to load grid view for showing photos
     */
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
}
