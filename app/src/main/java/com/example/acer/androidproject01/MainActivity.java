package com.example.acer.androidproject01;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static com.example.acer.androidproject01.Data.mylocalhost;

//import org.apache.http.client.fluent.Content;
//import org.apache.http.client.fluent.Form;
//import org.apache.http.client.fluent.Request;
//import org.apache.http.entity.ContentType;
//import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
//import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
//import org.junit.Test;


public class MainActivity extends AppCompatActivity {

    public String aandp;
    public String xx="你输入的账号密码错误或不存在，请重新输入；";
    int f=1;  //用于等待线程结束；
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public void signuponclick (View v)
    {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, SignupActivity.class);
        //intent.setClass(MainActivity.this, TakephotoActivity.class);//测试用转跳相机
        startActivity(intent);
    }

	/*public void gtac (String t)
	{
	//String Name = "The_name";

	//Activator.CreateInstance(Type.GetType(t));
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, Type.GetType(t).class);
		startActivity(intent);
	}*/

    public void getap(View v)
    {
//		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//
//		StrictMode.setThreadPolicy(policy);

        EditText e = (EditText) findViewById (R.id.acountlog);
        String account = e.getText().toString();
        e = (EditText) findViewById (R.id.password);
        String password = e.getText().toString();
        System.out.println(account+" "+password);

        //aandp ="http://10.0.2.2:8080/login?username="+account+"&password="+password;

        //sendtest();
        if (!(boolLength(password)&&boolLength(account)))
        {
            Toast.makeText(MainActivity.this, "账号密码长度为空或过长，请重新输入", Toast.LENGTH_LONG).show();
        }

        else
        {
            sendLogInfo(account, password);

            showalertdialog(xx);
        }
    }

    public static boolean boolLength(String str) {
        Pattern pattern = Pattern.compile(".{1,20}");
        return pattern.matcher(str).matches();
    }



    private void sendLogInfo(final String account, final String password) {
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
                       .url("http://"+mylocalhost+":8080/login")
 //               .url("http://119.29.55.101:8080/dango/login")
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
                            userx u = new userx();
                            JSONObject obj = new JSONObject(result);
                           // u.access_token = obj.getJSONObject("auth").getString("access_token");
                            u.readData();
                            if (u.access_token.equals(obj.getJSONObject("auth").getString("access_token")))
                            {}
                            else {
                                System.out.println("needface in main:\n"+u.needface+"\n token:\n"+u.access_token+" \n"+obj.getJSONObject("auth").getString("access_token"));
                                u.access_token = obj.getJSONObject("auth").getString("access_token");
                                u.needface = obj.getJSONObject("user").getBoolean("needface");
                            }

                            System.out.println("token:" +  u.access_token + ", needface:" + u.needface);

                            if(u.access_token!=null) {
                                xx="登录成功";
                                System.out.println("jump to TakephotoActivity");
                                u.initData();
                                if (u.needface)
                                {
                                    Intent intent = new Intent();
                                    intent.setClass(MainActivity.this, TakephotoActivity.class);

                                    /* 通过Bundle对象存储需要传递的数据 */
                                    Bundle bundle = new Bundle();
/*字符、字符串、布尔、字节数组、浮点数等等，都可以传*/
                                   // bundle.putString("Name", "feng88724");
                                    bundle.putBoolean("needface", true);

/*把bundle对象assign给Intent*/
                                    intent.putExtras(bundle);

                                    startActivity(intent);
                                   // showalertdialog("您尚未上传初始照片，请先拍摄初始照片 ");
                                    xx="您尚未上传初始照片，请先拍摄初始照片 ";
                                }
                                else
                                    {
                                        Intent intent = new Intent();
                                       intent.setClass(MainActivity.this, IndexActivity.class);
                                        startActivity(intent);
                                        //showalertdialog("登录成功 ");

                                    }

                            } else
                            {
                                xx="你输入的账号密码错误或不存在，请重新输入；";
                                System.out.println("wrong");

                            }



                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                f=0;
            }
        }).start();

       while(f==1)
       {
          // System.out.println("in the while , f= "+f);
       };
        f=1;

//        EditText etest = (EditText) findViewById (R.id.sm);
//        etest.setText(xx);


    }




    public void showalertdialog(String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("确认",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                    }
                });
        alertDialogBuilder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }







}


