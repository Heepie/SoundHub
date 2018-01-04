package com.heepie.soundhub.domain.logic;

import android.content.Context;
import android.util.Log;
import com.heepie.soundhub.Interfaces.ICallback;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

/**
 * Created by Heepie on 2017. 12. 5..
 */

public class FileApi {
    public final String TAG = getClass().getSimpleName();
    private static FileApi instance;
    private OkHttpClient client;

    public static FileApi getInstance() {
        if (instance == null) {
            instance = new FileApi();
        }
        return instance;
    }

    private FileApi() {
        client = new OkHttpClient();
    }

    public void getMusic(Context context, String url, String post_id, ICallback callback) {
        String path = context.getExternalCacheDir().getAbsolutePath() + File.separator + post_id + ".mp3";
        File futureStudioIconFile = new File(path);

        if (futureStudioIconFile.exists()) {
            futureStudioIconFile.delete();
        }

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.e("heepie", "fail");
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if(response.isSuccessful()){
                    ResponseBody body = response.body();
                    Log.e(TAG, "sucess");
                    try {
                        InputStream inputStream = null;
                        OutputStream outputStream = null;
                        try {
                            byte[] fileReader = new byte[4096];

                            long fileSize = body.contentLength();
                            long fileSizeDownloaded = 0;

                            inputStream = body.byteStream();
                            outputStream = new FileOutputStream(futureStudioIconFile);

                            while (true) {
                                int read = inputStream.read(fileReader);
                                if (read == -1) {
                                    break;
                                }
                                outputStream.write(fileReader, 0, read);
                                fileSizeDownloaded += read;
                            }

                            outputStream.flush();
                            callback.initData(200, "OK", path);

                        } catch (IOException e) {
                            Log.e(TAG, "error:"+e.toString());
                        } finally {
                            if (inputStream != null) {
                                inputStream.close();
                            }

                            if (outputStream != null) {
                                outputStream.close();
                            }
                        }
                    } catch (IOException e) {

                    }
                } else {
                    Log.e(TAG, "Not Successful");
                }
            }
        });
    }
}
