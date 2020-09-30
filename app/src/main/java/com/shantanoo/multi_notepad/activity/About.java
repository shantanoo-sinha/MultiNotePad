package com.shantanoo.multi_notepad.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.shantanoo.multi_notepad.R;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        getSupportActionBar().hide();
    }
}