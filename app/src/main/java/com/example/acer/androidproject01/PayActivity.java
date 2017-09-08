package com.example.acer.androidproject01;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class PayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
    }

    public void pay (View v)
    {
        Intent intent = new Intent();
        intent.setClass(PayActivity.this, TakephotoActivity.class);
        startActivity(intent);
    }
}
