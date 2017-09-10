package com.example.acer.androidproject01;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import okhttp3.*;
import org.apache.http.util.EncodingUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
        intent.setClass(MainActivity.this, IndexActivity.class);
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
        sendLogInfo(account, password);

        showalertdialog(xx);

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
                        .url("http://10.63.208.86:8080/login")
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
                            u.access_token = obj.getJSONObject("auth").getString("access_token");
                            u.needface = obj.getJSONObject("user").getBoolean("needface");
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


    class userx
    {
        String user;
        String auth;
        String username;
        String password;
        boolean needface;
        String access_token;
        String refresh_token;

        private void initData() {
            String filePath= Environment.getExternalStorageDirectory().toString()
                    + File.separator
                    +"AppTest"
                    + File.separator;

            String fileName = "userx.txt";
            String readtest = "userx.txt";
            try
            {
                readtest=readFile(filePath+fileName);
            }catch(Exception e){
                System.out.println("wrong:read file error01");
                e.printStackTrace();

            }

            deleteFile(filePath+fileName);
            deleteFile(filePath+fileName+"copy.txt");


            writeTxtToFile("access_token:"+userx.this.access_token, filePath, fileName);
            writeTxtToFile(" "+readtest, filePath, fileName+"copy.txt");
        }

        private void readData() {
            String filePath= Environment.getExternalStorageDirectory().toString()
                    + File.separator
                    +"AppTest"
                    + File.separator;

            String fileName = "userx.txt";
            String readtest = "userx.txt";
            try
            {
                readtest=readFile(filePath+fileName);
            }catch(Exception e){
                System.out.println("wrong:read file error01");
                e.printStackTrace();

            }

            userx.this.access_token=readtest;
            System.out.println("readtest= "+ readtest );

        }


        public String readFile(String fileName) throws IOException{
            String res="";
            try{
               // FileInputStream fin = openFileInput(fileName);
                FileInputStream fin = new FileInputStream(fileName);
                int length = fin.available();
                byte [] buffer = new byte[length];
                fin.read(buffer);
                res = EncodingUtils.getString(buffer, "gbk");
                System.out.println("res:"+res);
                fin.close();
            }
            catch(Exception e){
                System.out.println("wrong:read file error02");
                e.printStackTrace();
            }
            return res;

        }

        // 将字符串写入到文本文件中
        public void writeTxtToFile(String strcontent, String filePath, String fileName) {
            //生成文件夹之后，再生成文件，不然会出错
            makeFilePath(filePath, fileName);

            String strFilePath = filePath+fileName;
            // 每次写入时，都换行写
            String strContent = strcontent + "\r\n";
            try {
                File file = new File(strFilePath);
                if (!file.exists()) {
                    Log.d("TestFile", "Create the file:" + strFilePath);
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }
                RandomAccessFile raf = new RandomAccessFile(file, "rwd");
                raf.seek(file.length());
                raf.write(strContent.getBytes());
                raf.close();
            } catch (Exception e) {
                Log.e("TestFile", "Error on write File:" + e);
            }
        }

        public boolean deleteFile(String filePath) {
            File file = new File(filePath);
            if (file.isFile() && file.exists()) {
                return file.delete();
            }
            return false;
        }

        // 生成文件
        public File makeFilePath(String filePath, String fileName) {
            File file = null;
            makeRootDirectory(filePath);
            try {
                file = new File(filePath + fileName);
                if (!file.exists()) {
                    file.createNewFile();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return file;
        }

        // 生成文件夹
        public void makeRootDirectory(String filePath) {
            File file = null;
            try {
                file = new File(filePath);
                if (!file.exists()) {
                    file.mkdir();
                }
            } catch (Exception e) {
                Log.i("error:", e + "");
            }
        }

//        public  void writefilex()
//        {
//            String fileName= Environment.getExternalStorageDirectory().toString()
//                    + File.separator
//                    +"AppTest"
//                    + File.separator
//                    +"userx.txt";
//            File file=new File(fileName);
//            if(!file.getParentFile().exists()){
//                file.getParentFile().mkdir();//创建文件夹
//                file.createNewFile();
//            }
//            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
//            raf.seek(file.length());
//            raf.write(userx.this.access_token.getBytes());
//            raf.close();
//        }
    }




}


