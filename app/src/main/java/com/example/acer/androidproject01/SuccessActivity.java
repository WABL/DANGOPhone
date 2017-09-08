package com.example.acer.androidproject01;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
    }

    public void payback (View v)
    {
        Intent intent = new Intent();
        intent.setClass(SuccessActivity.this, IndexActivity.class);
        startActivity(intent);
    }

}
