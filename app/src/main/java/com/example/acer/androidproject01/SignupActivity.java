package com.example.acer.androidproject01;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    public void goBack (View v) {
        System.out.println("goBack");
        Intent intent = new Intent();
        intent.setClass(SignupActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void goOn (View v) throws IOException {
        System.out.println("goOn");

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
                System.out.println("connect");
                Map<String, String> map = new HashMap<>();
                map.put("username", account);
                map.put("password", password);
                JSONObject jsonObject = new JSONObject(map);
                final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

                RequestBody body = RequestBody.create(JSON, String.valueOf(jsonObject));
                Request request = new Request.Builder()
                        .url("http://10.63.208.86:8080/register")
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json, charset=utf-8")
                        .addHeader("Connection", "close")
                        .post(body)
                        .build();

                OkHttpClient client = new OkHttpClient();
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.connectTimeout(30, TimeUnit.SECONDS);
                builder.readTimeout(30, TimeUnit.SECONDS);
                builder.writeTimeout(30, TimeUnit.SECONDS);
                client = builder.build();
                try {

                    Response response = client.newCall(request).execute();
                    if(response.isSuccessful()) {
                        final String result = response.body().string();
                        if(!TextUtils.isEmpty(result)) {
                            JSONObject obj = new JSONObject(result);
                            String token = obj.getJSONObject("auth").getString("access_token");
                            boolean success = obj.getBoolean("success");
                            System.out.println("token:" + token + ", success:" + success);

                            if(success) {
                                System.out.println("jump to Takephotoactivity");
                                Intent intent = new Intent();
                                intent.setClass(SignupActivity.this, MainActivity.class);
                                startActivity(intent);

                            } else
                            {
                                System.out.println("wrong");
                            }

                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
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

