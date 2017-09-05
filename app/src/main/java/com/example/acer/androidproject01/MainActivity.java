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

import android.*;

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

        //
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, IndexActivity.class);
        startActivity(intent);
        System.out.println("LOGIN SUCSESS");//登录成功

        showalertdialog(xx);//弹窗错误
        //sendget(etest);


        //sendRequestWithHttpClient(etest);

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
