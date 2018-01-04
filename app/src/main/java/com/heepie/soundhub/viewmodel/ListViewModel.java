package com.heepie.soundhub.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.heepie.soundhub.Interfaces.ICallback;
import com.heepie.soundhub.domain.logic.DataAPI;
import com.heepie.soundhub.domain.model.Data;
import com.heepie.soundhub.domain.model.Post;
import com.heepie.soundhub.domain.model.User;
import com.heepie.soundhub.utils.Const;
import com.heepie.soundhub.view.MusicUploadView;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Created by Heepie on 2017. 11. 27..
 */

public class ListViewModel extends BaseObservable {
    public final String TAG = getClass().getSimpleName();
    private DataAPI dataAPI;
    private Data data;

    public String[][] category;

    @Bindable
    public UsersViewModel populUsers;

    @Bindable
    public PostsViewModel populPosts;

    @Bindable
    public PostsViewModel newPosts;
    private Context context;

    public ListViewModel(Context context) {
        populUsers = new UsersViewModel();
        populPosts = new PostsViewModel();
        newPosts = new PostsViewModel();
        this.context = context;
        category = new String[2][9];
    }

    public void resetData() {
        populUsers.users.clear();
        populPosts.posts.clear();
        newPosts.posts.clear();
    }

    @Bindable
    public String[][] getCategory() {
        return category;
    }

    public void setDisplayCategory() {
        // 더미 데이터
        category[0][0] = "Pop";
        category[0][1] = "Rock";
        category[0][2] = "Jazz";
        category[0][3] = "Hiphop";
        category[0][4] = "Electronic";
        category[0][5] = "Classic";
        category[0][6] = "Others";

        Log.d(TAG, "setDisplayCategory: " + category[0][7]);

        category[1][0] = "Vocal";
        category[1][1] = "Guitar";
        category[1][2] = "Bass";
        category[1][3] = "Drums";
        category[1][4] = "Keyboard";
        category[1][5] = "Others";
    }

    public void setUser() {

    }

    public void setDisplayData(String category, ICallback callback) {
        dataAPI = DataAPI.getInstance();
        Observable<Response<Data>> dataObs = dataAPI.getData(category);

        dataObs.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        jsonData -> {
                            // 네트워크로 입력 받은 데이터 DataModel에 셋팅
                            dataAPI.setModelData(jsonData.body());
                            data = jsonData.body();
                            setData(data);
                            callback.initData(Const.RESULT_SUCCESS, "Success", null);
                        });
    }

    // DataModel로부터 가져온 데이터 ViewModel 내부에 셋팅
    private void setData(Data data) {
        for (User user : data.getPop_users()) {
            populUsers.addUserViewModel(new UserViewModel(user));
        }

        // 최초 5개 설정
        for (int i=0; i<Const.DEFAULT_COUNT_OF_SHOW_ITEM; i=i+1) {
            if (data.getPop_posts().size() > i) {
                Post post = data.getPop_posts().get(i);
                populPosts.addPostViewModel(new PostViewModel(post));
            }
        }

        // 최초 5개 설정
        for (int i=0; i<Const.DEFAULT_COUNT_OF_SHOW_ITEM; i=i+1) {
            if (data.getRecent_posts().size() > i) {
                Post post = data.getRecent_posts().get(i);
                newPosts.addPostViewModel(new PostViewModel(post));
            }
        }
    }

    public void onClickedUpLoad(View v) {
        Intent intent = new Intent(context, MusicUploadView.class);
        context.startActivity(intent);
    }
}