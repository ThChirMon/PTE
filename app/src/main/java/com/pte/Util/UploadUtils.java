package com.pte.Util;

import android.util.Log;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadUtils {
    private static final String TAG = "UPLOADUTILS";

    public static void uploadFile(String filepath) {
        File file = new File(filepath);
        Thread thread = new Thread(() -> {
            RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", "img.jpg", fileBody);
            RequestBody requestBody = builder.build();

            Request request = new Request.Builder()
                    .url("http://192.168.31.244:8081/demo/upload")
                    .post(requestBody)
                    .build();

            OkHttpClient okHttpClient = new OkHttpClient();
            Call call = okHttpClient.newCall(request);
            try (Response response = okHttpClient.newCall(request).execute()) {
                String res = response.body().string();
                System.out.println(res);
            } catch (IOException e) {
                e.printStackTrace();
            }
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "failure upload!");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.i(TAG, "successful upload!");
                }

            });

        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }



}