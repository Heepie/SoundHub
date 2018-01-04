package com.heepie.soundhub.domain.logic;

import com.heepie.soundhub.BuildConfig;
import com.heepie.soundhub.domain.model.Data;
import com.heepie.soundhub.utils.Const;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Heepie on 2017. 12. 6..
 */

public class DataAPI {
    public final String TAG = getClass().getSimpleName();
    public static DataAPI instance;
    private Data modelData;

    private Retrofit retrofit;

    private DataAPI() {
        createRetrofit(BuildConfig.SERVER_URL);
    }

    public static DataAPI getInstance() {
        if (instance == null)
            instance = new DataAPI();
        return instance;
    }

    public void createRetrofit(String defaultURL) {
        Retrofit.Builder rBuilder = new Retrofit.Builder();

        // 기본 URL 설정
        rBuilder.baseUrl(defaultURL);

        // Gson 팩토리로 JSON 데이터 처리 설정
        rBuilder.addConverterFactory(GsonConverterFactory.create());

        // RxJava2 어뎁터 사용 설정
        rBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        // 해당 Retrofit 생성
        retrofit = rBuilder.build();
    }

    public Observable<Response<Data>> getData(String category) {
        IData service = retrofit.create(IData.class);

        // 카테고리 별 분기
        if (category == Const.CATEGORY_DEFAULT)
            return service.getData("");

        return service.getData(category);
    }

    public Data getModelData() {
        return modelData;
    }

    public void setModelData(Data modelData) {
        this.modelData = modelData;
    }

    public interface IData {
        @GET("home/{category}")
        Observable<Response<Data>> getData(@Path("category") String category);
    }
}
