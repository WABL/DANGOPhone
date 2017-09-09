package com.example.acer.androidproject01;

import okhttp3.*;

import java.io.File;
import java.io.IOException;

/**
 * Created by 54472 on 2017/9/7.
 */
public class uploadImage {
    public void uploadImage() {
        File file = new File("C:\\Users\\54472\\Desktop\\test.jpg");
        OkHttpClient client = new OkHttpClient();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        client = builder.build();
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("photo", "test.jpg", requestBody)
                .build();

        Request request = new Request.Builder()
                .url("http://localhost:8080/face/upload/register")
                .post(multipartBody)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer 699d8d7c-9f05-49b2-9814-bf5555c87990")
                .addHeader("Content-Type", "multipart/form-data; charset=utf-8;")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println(response.body().string());
            }
        });
    }

}
