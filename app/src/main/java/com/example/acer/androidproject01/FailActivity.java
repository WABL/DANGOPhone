package com.example.acer.androidproject01;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class FailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fail);
    }

    public void payback (View v)
    {

        Intent intent = new Intent();
        intent.setClass(FailActivity.this, IndexActivity.class);
        startActivity(intent);
    }
}
