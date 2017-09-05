package com.example.acer.androidproject01;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.*;


import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
//import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
//import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.json.JSONObject;
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
        String acount = e.getText().toString();
        e = (EditText) findViewById (R.id.password);
        String password = e.getText().toString();
        System.out.println(acount+" "+password);

        aandp ="http://10.0.2.2:8080/login?username="+acount+"&password="+password;

        sendtest();

    }

    //http://localhost:8080/login?username=user&password=USER

    public void sendtest()
    {
        EditText etest = (EditText) findViewById (R.id.sm);
        etest.setText("log in ");
        //sendget(etest);


        sendRequestWithHttpClient(etest);

    }

    private void sendRequestWithHttpClient( EditText etest ) {
        new Thread(new Runnable() {

            @Override
            public void run() {



                //用HttpClient发送请求，分为五步
                //第一步：创建HttpClient对象
                HttpClient httpCient = new DefaultHttpClient();
                //第二步：创建代表请求的对象,参数是访问的服务器地址
                String uriAPI = aandp;

                //"http://www.baidu.com";

                HttpGet httpGet = new HttpGet(uriAPI);

                try {
                    //第三步：执行请求，获取服务器发还的相应对象
                    HttpResponse httpResponse = httpCient.execute(httpGet);
                    //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        //第五步：从相应对象当中取出数据，放到entity当中
                        HttpEntity entity = httpResponse.getEntity();
                        String response = EntityUtils.toString(entity,"utf-8");//将entity当中的数据转换为字符串

                        //在子线程中将Message对象发出去
                        Message message = new Message();
                        //    message.what = SHOW_RESPONSE;
                        message.obj = response.toString();
                        //    handler.sendMessage(message);
                        System.out.println(message+"233");
                        //   showalertdialog("看看");
                        System.out.println(message.toString()+"666");
                        // etest.setText(message.toString());

                        //if(message.toString().equals("YES"))

                        if( message.toString().indexOf("YES")>=0)
                        {
                            xx="登录成功";
                            Intent intent = new Intent();
                            intent.setClass(MainActivity.this, IndexActivity.class);
                            startActivity(intent);
                            System.out.println(message+"LOGIN SUCSESS");

                        }
                        else
                        {
                            xx="你输入的账号密码错误或不存在，请重新输入；";
                            //	showalertdialog("看看");
                        	/*new  AlertDialog.Builder(MainActivity.this)
                        	                .setTitle("登录失败" )
                        	                .setMessage("你输入的账号密码错误或不存在，请重新输入；")
                        	                .setPositiveButton("确定" ,  null)
                        	                .show();  */
//                        	EditText etest = (EditText) findViewById (R.id.sm);
//                        	etest.setText("你输入的账号密码错误或不存在，请重新输入；");
//                        	AlertDialog.Builder builder = new Builder(MainActivity.this);//getBaseContext());
//                        	builder.setTitle("登录失败");
//                        	builder.setMessage("你输入的账号密码错误或不存在，请重新输入；");
//                        	builder.setPositiveButton("确定", null);
//                        	AlertDialog dialog = builder.create();	//创建对话框
//                        	dialog.setCanceledOnTouchOutside(true);	//设置弹出框失去焦点是否隐藏,即点击屏蔽其它地方是否隐藏
//                        	dialog.show();

//                        	AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(MainActivity.this);
//                        	AlertDialog alertDialog = alertDialogBuilder.create();
//                            alertDialog.show();//将dialog显示出来

                            //new AlertDialog.Builder(getParent()).create().show();

                            System.out.println(message+"LOGIN FAILS");

                        }
                        f=0;
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }).start();//这个start()方法不要忘记了
        //EditText etest = (EditText) findViewById (R.id.sm);
        while(f==1){};
        f=1;
        etest.setText(xx);
        showalertdialog(xx);

    }

    public void sendget(EditText etest)
    {
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //创建子线程才能访问网络；

		/*URL可以随意改*/
        String uriAPI =
                "http://wthrcdn.etouch.cn/weather_mini?city=北京";
        //"http://www.baidu.com";
        //"http://10.0.2.2:8080/demo";
        //"http://169.254.16.196:8080/demo";
        /*建立HTTP Get对象*/
        HttpClient httpCient = new DefaultHttpClient();
        HttpGet httpRequest = new HttpGet(uriAPI);
        try
        {
          /*发送请求并等待响应*/
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
          /*若状态码为200 ok*/
            if(httpResponse.getStatusLine().getStatusCode() == 200)
            {
            /*读*/
                String strResult = httpResponse.getEntity().toString();
            /*去没有用的字符*/
                //strResult = eregi_replace("(\r\n|\r|\n|\n\r)","",strResult);
                etest.setText(strResult);
                System.out.println(strResult+"233");
            }
            else
            {
                etest.setText("Error Response: "+httpResponse.getStatusLine().toString());
                System.out.println("erro;;");
            }
        }
        catch (ClientProtocolException e)
        {
            etest.setText(e.getMessage().toString());
            e.printStackTrace();
        }
        catch (IOException e)
        {
            etest.setText(e.getMessage().toString());
            e.printStackTrace();
        }
        catch (Exception e)
        {
            // etest.setText(e.getMessage().toString());
            e.printStackTrace();
        }
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
