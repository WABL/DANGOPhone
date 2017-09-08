package com.example.acer.androidproject01;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class IndexActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
    }

    public void goback (View v)
    {
        Intent intent = new Intent();
        intent.setClass(IndexActivity.this, MainActivity.class);
        startActivity(intent);
    }
    public void gotp (View v)
    {
        Intent intent = new Intent();
        intent.setClass(IndexActivity.this, PayActivity.class);
        startActivity(intent);
    }
}

