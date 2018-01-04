package com.heepie.soundhub.domain.logic;

import android.arch.persistence.room.Delete;
import android.databinding.ObservableArrayList;
import android.util.Log;

import com.heepie.soundhub.BuildConfig;
import com.heepie.soundhub.Interfaces.ICallback;
import com.heepie.soundhub.domain.model.User;
import com.heepie.soundhub.viewmodel.InputViewModel;
import com.heepie.soundhub.viewmodel.UserViewModel;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.adapter.rxjava2.Result;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by Heepie on 2017. 11. 30..
 */

public class UserApi extends AbsApi {
    public static int populUserIndex = 0;
    private static UserApi instance;
    public static ObservableArrayList<UserViewModel> users = new ObservableArrayList<>();

    private UserApi() {

    }

    public static UserApi getInstance() {
        if (instance == null)
            instance = new UserApi();
        return instance;
    }

    public void getData(ICallback callback) {
        getData("",callback);
    }

    public void getData(String category, ICallback callback) {
        // 데이터 초기화
        users.clear();

//        서버 측 완료시 제거
        createRetrofit(BuildConfig.SERVER_URL);

        // Retrofit으로 서비스 생성
        IUser server = retrofit.create(IUser.class);

        // 서비스를 생성
        Observable<Response<List<User>>> observable = server.getUser();

        // 카테고리 데이터 서버로부터 전달 받을 시, 주석 제거
//        Observable<Response<List<User>>> observable = server.getUser(category);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        user -> {
//                            서버와 통신 테스트
                            Log.e("heepie", user.code() + "\n" +
                                    user.message() + "\n" +
                                    user.body());

//                            입력 받은 데이터 확인
                            for (User item : user.body()) {
                                users.add(new UserViewModel(item));
                            }

                            callback.initData(user.code(), user.message(), user.body());
                        });

    }

    public interface IUser {
        @GET("user/")
        Observable<Response<List<User>>> getUser();

        @GET("user/{parm1}")
        Observable<Response<List<User>>> getUser(@Path("parm1") String category);

        @Multipart
        @POST("user/signup/")
        Call<Result> signUpUser(@Part("email") RequestBody email,
                                @Part("password1") RequestBody password1,
                                @Part("password2") RequestBody password2,
                                @Part("instrument") RequestBody instrument,
                                @Part("nickname") RequestBody nickname);

        @Multipart
        @POST("user/login/")
        Call<InputViewModel.LoginResult> getLogin(@Part("email") RequestBody email,
                                                  @Part("password") RequestBody password);

        @Multipart
        @PATCH("user/{id}/")
        Call<User> getModify(@Path("id") String id,
                             @Header("Authorization")String token,
                             @Part("nickname")RequestBody nickname,
                             @Part("instrument")RequestBody instrument,
                             @Part("genre")RequestBody genre);

        @DELETE("user/{id}/")
        Call<Result> signOut(@Path("id") String id, @Header("Authorization") String token);

        @GET("user/{id}/")
        Call<User> get1User(@Path("id") String id);

        @Multipart
        @PATCH("user/{id}/profile-img/")
        Call<Result> modifyProimg(@Path("id") String id,
                                  @Header("Authorization") String token,
                                  @Part MultipartBody.Part profile_img);

//        @PATCH("user/{id}/profile-img/")
//        Call<Result> modifyBackimg(@Path("id") String id,
//                                   @Header("Authorization") String token,
//                                   @Part MultipartBody.Part profile_bg);
    }
}
