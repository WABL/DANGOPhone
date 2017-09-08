package com.example.acer.androidproject01;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import android.hardware.*;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.*;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.lang.Math;

public class TakephotoActivity extends AppCompatActivity {


    private Camera mCamera;
    private CameraPreview mPreview;

    private SensorManager mSensorManager = null;
    private Sensor mSensor = null;
    private float x, y, z,tanx;
    double f;
    String flag;
    private ImageView image;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_takephoto);

        mSensorManager = (SensorManager)this.getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        ImageView imga = (ImageView) findViewById(R.id.image_3);
        //imga.setAlpha(0);

        // Create an instance of Camera
        mCamera = getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
    }

    SensorEventListener lsn = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            x = event.values[SensorManager.DATA_X];
            y = event.values[SensorManager.DATA_Y];
            z = event.values[SensorManager.DATA_Z];
            tanx = z / y;

            f = Math.atan(tanx) * 180 / 3.1415926;

            flag = "false";
            if (f > -10 && f < 10) {
                flag = "true";
            }

            try {
                Thread.currentThread().sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            mSensorManager.registerListener(lsn, mSensor, SensorManager.SENSOR_DELAY_GAME);
            String str = "x=" + x + "; y=" + y + "; z=" + z + "; tanx=" + tanx + "; f=" + f + "; flag=" + flag;
            //Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();

            String str2="当前角度："+(int)f+"是否可以拍照："+flag;
            //EditText etest = (EditText) findViewById(R.id.information2);
            TextView etest = (TextView) findViewById(R.id.information2);
            etest.setText(str2);
            if (flag=="false")
            {
                etest.setTextColor(0xffff0000);
            }
            else etest.setTextColor(0xff00BB00);

        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub
        }
    };

    @Override
    public void onResume(){
        mSensorManager.registerListener(lsn, mSensor, SensorManager.SENSOR_DELAY_GAME);
        super.onResume();
    }
    @Override
    public void onPause(){
        mSensorManager.unregisterListener(lsn);
        super.onPause();
    }



    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    public static Camera getCameraInstance(){
        /*
        //前置摄像头
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
        */

        Camera camera = null;
//调用前置摄像头的代码；
//如果想后置摄像头，可直接Camera.open();默认就是后置的
        Camera.CameraInfo info= new Camera.CameraInfo();
        int count= Camera.getNumberOfCameras();
        for (int i = 0; i < count; i++) {
            Camera.getCameraInfo(i, info);
            if (info.facing==Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {

//打开前置相机
                    camera = Camera.open(i);
                    camera.setDisplayOrientation(90);

                    //  setCameraDisplayOrientation(t.this,Camera.CameraInfo.CAMERA_FACING_FRONT,camera);

                } catch (RuntimeException e) {
// TODO: handle exception
                }
            }

        }

        return camera;

    }

    public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
        private SurfaceHolder mHolder;
        private Camera mCamera;

        public CameraPreview(Context context, Camera camera) {
            super(context);
            mCamera = camera;

            // Install a SurfaceHolder.Callback so we get notified when the
            // underlying surface is created and destroyed.
            mHolder = getHolder();
            mHolder.addCallback(this);
            // deprecated setting, but required on Android versions prior to 3.0
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        public void surfaceCreated(SurfaceHolder holder) {
            // The Surface has been created, now tell the camera where to draw the preview.
            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            } catch (IOException e) {
                //   Log.d(TAG, "Error setting camera preview: " + e.getMessage());
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            // empty. Take care of releasing the Camera preview in your activity.
            //释放相机
            if (mCamera != null) {
                mCamera .stopPreview();
                mCamera .release();
                mCamera = null;
            }
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            // If your preview can change or rotate, take care of those events here.
            // Make sure to stop the preview before resizing or reformatting it.

            if (mHolder.getSurface() == null){
                // preview surface does not exist
                return;
            }

            // stop preview before making changes
            try {
                mCamera.stopPreview();
            } catch (Exception e){
                // ignore: tried to stop a non-existent preview
            }

            // set preview size and make any resize, rotate or
            // reformatting changes here

            // start preview with new settings
            try {
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();

            } catch (Exception e){
                //   Log.d(TAG, "Error starting camera preview: " + e.getMessage());
                // Log.i("123--savedInstanceState", "Error starting camera preview: " + e.getMessage());
            }
        }
    }

    public class WrapCameraSize implements Comparable<WrapCameraSize> {
        private int width;//宽
        private int height;//高
        private int d;//宽的差的绝对值和高的差的绝对值之和

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getD() {
            return d;
        }

        public void setD(int d) {
            this.d = d;
        }


        @Override
        public int compareTo(WrapCameraSize another) {
            if (this.d > another.d) {
                return 1;
            } else if (this.d < another.d) {
                return -1;
            }
            return 0;
        }
    }

    /*
   * 设置相机Preview Size 和 Picture Size，找到设备所支持的最匹配的相机预览和图片大小
   * */
    private void setCameraSize(Camera.Parameters parameters, int width, int height) {
        Map<String, List<Camera.Size>> allSizes = new HashMap<>();
        String typePreview = "typePreview";
        String typePicture = "typePicture";
        allSizes.put(typePreview, parameters.getSupportedPreviewSizes());
        allSizes.put(typePicture, parameters.getSupportedPictureSizes());
        Iterator iterator = allSizes.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<Camera.Size>> entry = (Map.Entry<String, List<Camera.Size>>) iterator.next();
            List<Camera.Size> sizes = entry.getValue();
            if (sizes == null || sizes.isEmpty()) continue;
            ArrayList<WrapCameraSize> wrapCameraSizes = new ArrayList<>(sizes.size());
            for (Camera.Size size : sizes) {
                WrapCameraSize wrapCameraSize = new WrapCameraSize();
                wrapCameraSize.setWidth(size.width);
                wrapCameraSize.setHeight(size.height);
                wrapCameraSize.setD(Math.abs((size.width - width)) + Math.abs((size.height - height)));
                if (size.width == width && size.height == height) {
                    if (typePreview.equals(entry.getKey())) {
                        parameters.setPreviewSize(size.width, size.height);
                    } else if (typePicture.equals(entry.getKey())) {
                        parameters.setPictureSize(size.width, size.height);
                    }
                    //   Log.d(TAG, "best size: width=" + size.width + ";height=" + size.height);
                    break;
                }
                wrapCameraSizes.add(wrapCameraSize);
            }
            // Log.d(TAG, "wrapCameraSizes.size()=" + wrapCameraSizes.size());
            Camera.Size resultSize = null;
            if (typePreview.equals(entry.getKey())) {
                resultSize = parameters.getPreviewSize();
            } else if (typePicture.equals(entry.getKey())) {
                resultSize = parameters.getPictureSize();
            }
            if (resultSize != null) {
                if (resultSize.width != width && resultSize.height != height) {
                    //找到相机Preview Size 和 Picture Size中最适合的大小
                    WrapCameraSize minCameraSize = Collections.min(wrapCameraSizes);
                    while (!(minCameraSize.getWidth() >= width && minCameraSize.getHeight() >= height)) {
                        wrapCameraSizes.remove(minCameraSize);
                        minCameraSize = null;
                        minCameraSize = Collections.min(wrapCameraSizes);
                    }
                    //  Log.d(TAG, "best min size: width=" + minCameraSize.getWidth() + ";height=" + minCameraSize.getHeight());
                    if (typePreview.equals(entry.getKey())) {
                        parameters.setPreviewSize(minCameraSize.getWidth(), minCameraSize.getHeight());
                    } else if (typePicture.equals(entry.getKey())) {
                        parameters.setPictureSize(minCameraSize.getWidth(), minCameraSize.getHeight());
                    }
                }
            }
            iterator.remove();
        }
    }

    public static void setCameraDisplayOrientation(Activity activity,
                                                   int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    private android.hardware.Camera.PictureCallback mPicture = new android.hardware.Camera.PictureCallback() {

        @Override
        /*public void onPictureTaken(byte[] data, Camera camera) {

            Bitmap resultBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

        }*/

        public void onPictureTaken(byte[] data, Camera camera) {
            // 保存图片操作
            Bitmap bmp=BitmapFactory.decodeByteArray(data, 0, data.length);
            String fileName= Environment.getExternalStorageDirectory().toString()
                    +File.separator
                    +"AppTest"
                    + File.separator
                    +"Pictest.jpg";
            File file=new File(fileName);
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdir();//创建文件夹
            }
            try {
                BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(file));
                bmp.compress(Bitmap.CompressFormat.JPEG, 80, bos);//向缓冲区压缩图片
                bos.flush();
                bos.close();
                Toast.makeText(TakephotoActivity.this, "拍照成功，照片保存在"+fileName+"文件之中！", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
                Toast.makeText(TakephotoActivity.this, "拍照失败！"+e.toString(), Toast.LENGTH_LONG).show();
            }
            TakephotoActivity.this.mCamera.stopPreview();
            TakephotoActivity.this.mCamera.startPreview();
        }
    };

    // Add a listener to the Capture button
    // Button captureButton = (Button) findViewById(id.button_capture);


    public void takep(View v) {
        // get an image from the camera
        if(flag=="false")
        {
            Toast.makeText(TakephotoActivity.this, "拍照失败！请将手机竖直拿起，并在角度接近0时拍摄；", Toast.LENGTH_LONG).show();
        }
        else
            {
            mCamera.takePicture(null, null, mPicture);
            /*获取Intent中的Bundle对象*/
                Bundle bundle = this.getIntent().getExtras();

        /*获取Bundle中的数据，注意类型和key*/
               // String name = bundle.getString("Name");
                boolean needface = bundle.getBoolean("needface");
                if (needface)
                {
                    Intent intent = new Intent();
                    intent.setClass(TakephotoActivity.this, IndexActivity.class);
                    startActivity(intent);
                }
                else
                    {
                        if (Data.getA()=="0")
                        {
                            Data.setA("1");
                            Intent intent = new Intent();
                            intent.setClass(TakephotoActivity.this, SuccessActivity.class);
                            startActivity(intent);
                        }
                        else
                            {
                                Data.setA("0");
                                Intent intent = new Intent();
                                intent.setClass(TakephotoActivity.this, FailActivity.class);
                                startActivity(intent);
                            }
                    }
            }
    }

    ;


   /* Matrix matrix = new Matrix();
              if (mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
        matrix.postRotate(-mRotationDegrees);
    } else if (mCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
        matrix.postRotate(mRotationDegrees);
    }
    Bitmap rotateBitmap = Bitmap.createBitmap(resultBitmap, 0, 0, resultBitmap.getWidth(), resultBitmap.getHeight(), matrix, true);
*/



}