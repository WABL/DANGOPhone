package com.example.acer.androidproject01;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.githang.stepview.StepView;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static com.example.acer.androidproject01.Data.mylocalhost;

//import static com.example.acer.androidproject01.Data.usernow;

public class SignupActivity extends AppCompatActivity {
    private StepView mStepView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mStepView = (StepView) findViewById(R.id.step_view);
        List<String> steps = Arrays.asList(new String[]{"注册账号", "上传照片", "注册完成"});
        mStepView.setSteps(steps);
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

        if (!(isLength(password)&&isLength(account)))
        {
            Toast.makeText(SignupActivity.this, "账号密码长度小于6位，请重新输入", Toast.LENGTH_LONG).show();
        }
        boolean pass = validation(password, repassword)&&isLength(password)&&isLength(account);

        if(pass) {
            sendRegisterInfo(account, password);
        }

    }


    public static boolean isLength(String str) {
        Pattern pattern = Pattern.compile(".{6,20}");
        return pattern.matcher(str).matches();
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
                        .url("http://"+mylocalhost+":8080/register")
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

//                                Data.usernow.access_token=token;
//                                Data.usernow.needface=true;
                                userx us=new userx();
                                us.needface=true;
                                us.access_token=token;
                                us.initData();

                                Intent intent = new Intent();
                                intent.setClass(SignupActivity.this, PictureupsActivity.class);


                                startActivity(intent);

                            } else
                            {
                                System.out.println("wrong");
                                mistakeDialog();
                            }

                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    mistakeDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                    mistakeDialog();
                }

            }
        }).start();
    }

    private boolean validation(String password, String repassword) {
        if(password.equals(repassword)) {
            return true;
        }
        else {
            new AlertDialog.Builder(SignupActivity.this)
                    .setTitle("提示")
                    .setMessage("两次密码不一致")
                    .setPositiveButton("知道了",null)
                    .show();
            return false;
        }

    }

    public void mistakeDialog() {
        new AlertDialog.Builder(SignupActivity.this)
                .setTitle("Message")
                .setMessage("some mistakes")
                .setPositiveButton("I know",null)
                .show();
    }

}

