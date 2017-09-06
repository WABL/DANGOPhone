package com.example.acer.androidproject01;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    public void goBack (View v) {
        Intent intent = new Intent();
        intent.setClass(SignupActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void goOn (View v) throws IOException {

        EditText e = (EditText) findViewById(R.id.acountlog);
        String account = e.getText().toString();
        e = (EditText) findViewById(R.id.password);
        String password = e.getText().toString();
        e = (EditText) findViewById(R.id.repassword);
        String repassword = e.getText().toString();

        boolean pass = validation(password, repassword);

        if(pass) {
            sendRegisterInfo(account, password);
        }

    }

    private void sendRegisterInfo(final String account, final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = URL + "/register";
                Content content = null;
                try {
                    content = Request.Post(url)
                            .addHeader("Accept","application/json")
                            .addHeader("Content-Type", "application/json; charset=utf-8")
                            .bodyString("{\"username\": \""+account+"\",\"password\": \""+password+"\"}", ContentType.APPLICATION_JSON)
                            .execute().returnContent();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(content.toString());

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(content.toString());
                    String feedBack = jsonObject.getString("success");
                    if(feedBack.equals("success")) {
                        Intent intent = new Intent();
                        intent.setClass(SignupActivity.this, MainActivity.class);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }).start();
    }

    private boolean validation(String password, String repassword) {
        if(password.equals(repassword)) {
            return true;
        }
        else
            return false;
    }

}

