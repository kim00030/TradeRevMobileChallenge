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

    /**
     * Callback from full screen
     * @param requestCode code sent to full screen activity {@link SlideShowActivity} from this activity
     * @param resultCode  code sent from {@link SlideShowActivity}
     * @param data data send from {@link SlideShowActivity}
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check the condition if data is sent from full screen
        if (requestCode == SlideShowActivity.REQ_START_SLIDE_PHOTO && resultCode == Activity.RESULT_OK) {
            //set position of the item viewed lastly in full screen
            currentPosition = data != null ? data.getIntExtra(Constants.KEY_CURRENT_POSITION, 0) : 0;
            // update position of item in Grid view screen
            GridViewFragment fragment = (GridViewFragment) getSupportFragmentManager().findFragmentByTag(GridViewFragment.class.getSimpleName());
            if (fragment != null) {
                fragment.updateCurrentPosition(currentPosition);
            }
        }
    }
}
