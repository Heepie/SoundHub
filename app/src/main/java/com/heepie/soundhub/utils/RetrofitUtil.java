package com.heepie.soundhub.utils;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by eunmin on 2017-12-04.
 */
public class RetrofitUtil {
    private static String SERVER_URL = "https://soundhub.che1.co.kr";
    private static Retrofit retrofit;

    public static Retrofit getInstance() {
        if(null == retrofit) {
            // OkHttp Client 생성
            HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient okClient = new OkHttpClient
                    .Builder()
                    .addInterceptor(logInterceptor)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(SERVER_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okClient)
                    .build();
        }

        return retrofit;
    }
}
