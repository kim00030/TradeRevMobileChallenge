package com.dan.traderevmobilechallenge.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.dan.traderevmobilechallenge.R;

public class DummyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
