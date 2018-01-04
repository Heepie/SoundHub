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
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by Heepie on 2017. 11. 29..
 */

public class PostApi extends AbsApi {
    public final String TAG = getClass().getSimpleName();
    final String SEPARATED = ", ";

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

    public Observable<Response<List<Post>>> getPosts(int type, int page_num) {
        IPost server = retrofit.create(IPost.class);
        Observable<Response<List<Post>>> ret;
        switch (type) {
            case Const.VIEW_TYPE_POPULAR_POST:
                ret = server.getPopulPosts(page_num);
                break;

            case Const.VIEW_TYPE_NEW_POST:
                ret = server.getNewPosts(page_num);
                break;
            default:
                ret = null;
        }
        return ret;
    }

    public void pushLike(String post_id, ICallback callback) {
        IPost service = retrofit.create(IPost.class);
        Call<Post> result = service.pushLike(post_id, "Token " + Const.TOKEN);

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

    public void requestMerge(String post_id, List<String> mix_tracks, ICallback callback) {

        IPost service = retrofit.create(IPost.class);
        StringBuilder strBuilder = new StringBuilder();

        for (String track_id : mix_tracks) {
            Log.d(TAG, "requestMerge1: " + track_id);
            strBuilder.append(track_id).append(SEPARATED);
        }

        String sumString = strBuilder.toString();

        String mixTrack = sumString.substring(0, sumString.length() - SEPARATED.length());

        RequestBody mixTrackBody = RequestBody.create(MediaType.parse("text/plain"), mixTrack);

        Log.d(TAG, "requestMerge: " + post_id + " " + "Token " + Const.TOKEN + " " + mixTrack);

        Call<Post> result = service.requestMerge(post_id,"Token " + Const.TOKEN, mixTrackBody);
        result.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                Log.d(TAG, "onResponse: " + response.code());
                if (response.isSuccessful()) {
                    Log.d(TAG, "requestMerge: " + response.code() + " " + response.message() + " " + response.body());
                    callback.initData(response.code(), response.message(), response.body());
                } else {
                    Log.d(TAG, "requestMerge: " + "Fail");
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Log.d(TAG, "requestMerge: " + "Fail__");
            }
        });
    }

    public interface IPost {
        @GET("post/{post_id}/")
        Observable<Response<Post>> getPost(@Path("post_id") String post_id);

        // TODO Add Pagenagtion URL & Path
        @GET("")
        Observable<Response<List<Post>>> getNewPosts(@Path("") int page_num);

        // TODO Add Pagenagtion URL & Path
        @GET("")
        Observable<Response<List<Post>>> getPopulPosts(@Path("") int page_num);

        @POST("post/{post_id}/like/")
        Call<Post> pushLike(@Path("post_id") String post_id,
                            @Header("Authorization")String token);

        @Multipart
        @POST("post/")
        Call<Post> getLogin(@Header("Authorization") String token,
                            @Part("title") RequestBody title,
                            @Part("instrument") RequestBody instrument,
                            @Part("genre") RequestBody genre,
                            @Part("bpm") RequestBody bpm,
                            @Part MultipartBody.Part author_track);

        @Multipart
        @PATCH("post/{post_id}/mix/")
        Call<Post> requestMerge (@Path("post_id") String post_id,
                                 @Header("Authorization") String token,
                                 @Part("mix_tracks") RequestBody mix_tracks);
    }
}
