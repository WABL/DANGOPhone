package com.example.acer.androidproject01;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class IndexActivity extends AppCompatActivity {

    WebView webView;
    String url = "http://m.ctrip.com/html5/?allianceid=4897&sid=155950&ouid=000401app-&utm_medium=&utm_campaign=&utm_source=&isctrip=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

//        webView.setWebViewClient(new WebViewClient(){
//
//            String url = "http://m.shihuo.cn/digital?haojiaId=5G4Jae#qk=running4";
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//        });
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

