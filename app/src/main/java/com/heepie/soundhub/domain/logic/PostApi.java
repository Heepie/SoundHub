package com.heepie.soundhub.domain.logic;

/**
 * Created by Heepie on 2017. 11. 30..
 */

import android.databinding.ObservableArrayList;
import android.util.Log;

import com.heepie.soundhub.BuildConfig;
import com.heepie.soundhub.Interfaces.ICallback;
import com.heepie.soundhub.domain.model.Post;
import com.heepie.soundhub.utils.Const;
import com.heepie.soundhub.viewmodel.PostViewModel;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by Heepie on 2017. 11. 29..
 */

public class PostApi extends AbsApi {
    public final String TAG = getClass().getSimpleName();

    private static PostApi instance;
    public static ObservableArrayList<PostViewModel> posts = new ObservableArrayList<>();

    private PostApi() {
        createRetrofit(BuildConfig.SERVER_URL);
    }

    public static PostApi getInstance() {
        if (instance == null)
            instance = new PostApi();
        return instance;
    }

    public Observable<Response<Post>> getPost(String post_id) {
        IPost server = retrofit.create(IPost.class);
        return server.getPost(post_id);
    }

    public void pushLike(String post_id, ICallback callback) {
        IPost service = retrofit.create(IPost.class);

        Call<Post> result = service.pushLike(post_id,
                                             "Token " + Const.TOKEN);

        result.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: " + response.code() + " " + response.body());
                    callback.initData(response.code(), response.message(), response.body());
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });
    }

    public interface IPost {
        @GET("post/{post_id}/")
        Observable<Response<Post>> getPost(@Path("post_id") String post_id);

        @Multipart
        @POST("post/")
        Call<Post> getLogin(@Header("Authorization") String token, @Part("title") RequestBody title, @Part("instrument") RequestBody instrument, @Part("genre") RequestBody genre, @Part MultipartBody.Part author_track);


        @POST("post/{post_id}/like/")
        Call<Post> pushLike(@Path("post_id") String post_id,
                            @Header("Authorization")String token);
    }
}
