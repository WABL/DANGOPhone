package com.example.acer.androidproject01;

import android.os.Environment;
import android.util.Log;
import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by acer on 2017/9/11.
 */
public class userx
{
    String user;
    String auth;
    String username;
    String password;
    boolean needface;
    String access_token;
    String refresh_token;
    static  String signups="false";

 //   public  static  userx usernow;

    public void initData() {
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


        writeTxtToFile("access_token:"+userx.this.access_token+" needface:" +userx.this.needface, filePath, fileName);
        writeTxtToFile(" "+readtest, filePath, fileName+"copy.txt");
    }

    public void readData() {
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

        String s=(readtest.substring(readtest.indexOf("needface")+9));
        //userx.this.needface=Boolean.valueOf(s).booleanValue();
        if (s.indexOf("true")>=0)
        userx.this.needface=true;

        //userx.this.access_token=readtest;
        userx.this.access_token=(readtest.substring(readtest.indexOf("access_token")+13,readtest.indexOf("needface")-1));
        System.out.println("readtest= "+ readtest );
        System.out.println("needface pre = "+ s );
        System.out.println("needface = "+ userx.this.needface );
        System.out.println("access_token = "+ userx.this.access_token );

    }


    public String readFile(String fileName) throws IOException {
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