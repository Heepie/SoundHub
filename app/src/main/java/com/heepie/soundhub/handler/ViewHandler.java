package com.heepie.soundhub.handler;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.heepie.soundhub.R;
import com.heepie.soundhub.domain.logic.DataAPI;
import com.heepie.soundhub.domain.logic.PostApi;
import com.heepie.soundhub.domain.model.Data;
import com.heepie.soundhub.domain.model.Post;
import com.heepie.soundhub.domain.model.User;
import com.heepie.soundhub.utils.Const;
import com.heepie.soundhub.utils.SignUtil;
import com.heepie.soundhub.view.DetailView;
import com.heepie.soundhub.view.PageView;
import com.heepie.soundhub.view.UserPageView;
import com.heepie.soundhub.viewmodel.PostViewModel;
import com.heepie.soundhub.viewmodel.PostsViewModel;


import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Created by Heepie on 2017. 11. 30..
 */

public class ViewHandler {
    public final String TAG = getClass().getSimpleName();
    private PostApi postAPI;
    private int newPostIndex;
    private int populPostIndex;
    public static ViewHandler intance;

    private ViewHandler() {
        newPostIndex   = Const.DEFAULT_COUNT_OF_SHOW_ITEM;
        populPostIndex = Const.DEFAULT_COUNT_OF_SHOW_ITEM;
    }

    public static ViewHandler getIntance() {
        if (intance == null)
            intance = new ViewHandler();
        return intance;
    }

    public void initIndex() {
        newPostIndex   = Const.DEFAULT_COUNT_OF_SHOW_ITEM;
        populPostIndex = Const.DEFAULT_COUNT_OF_SHOW_ITEM;
    }

    // '더보기' 버튼 클릭시 실행되는 메소드
    public void onClickedMoreBtn(View v , PostsViewModel viewModel) {
        int startIndex;
        Data data = DataAPI.getInstance().getModelData();

        switch (v.getId()) {
        case R.id.populMoreBtn:
            startIndex = populPostIndex;

            // MAX_COUNT_OF_SHOW_ITEM까지 보여주고 추가로 누르면 상세 페이지로 이동
            if (startIndex == Const.MAX_COUNT_OF_SHOW_ITEM) {
                Intent intent = new Intent(v.getContext(), PageView.class);
                intent.putExtra("type" , Const.VIEW_TYPE_POPULAR_POST);
                v.getContext().startActivity(intent);
            } else {
                for (int i=startIndex; i<startIndex+Const.DEFAULT_COUNT_OF_SHOW_ITEM; i=i+1) {
                    if (data.getPop_posts().size() > i) {
                        viewModel.addPostViewModel(new PostViewModel(data.getPop_posts().get(i)));
                        populPostIndex++;
                    }
                }
            }
            break;

        case R.id.newMoreBtn:
            // 서버 Api 추가 시 진행
            startIndex = newPostIndex;

            // MAX_COUNT_OF_SHOW_ITEM까지 보여주고 추가로 누르면 상세 페이지로 이동
            if (startIndex == Const.MAX_COUNT_OF_SHOW_ITEM) {
                Intent intent = new Intent(v.getContext(), PageView.class);
                intent.putExtra("type" , Const.VIEW_TYPE_NEW_POST);
                v.getContext().startActivity(intent);
            } else {
                for (int i=startIndex; i<startIndex+Const.DEFAULT_COUNT_OF_SHOW_ITEM; i=i+1) {
                    if (data.getRecent_posts().size() > i) {
                        viewModel.addPostViewModel(new PostViewModel(data.getRecent_posts().get(i)));
                        newPostIndex++;
                    }
                }
            }
            break;
        }
    }
    public void onClickPostItem(View v, String id, Activity activity, View image, View like, View comment) {
        final Post[] post = new Post[1];
        postAPI = PostApi.getInstance();
        Intent intent = new Intent(v.getContext(), DetailView.class);

        Observable<Response<Post>> postObs = postAPI.getPost(id);
        postObs.subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(
                   jsonData -> {
                       if (jsonData.isSuccessful())
                           post[0] = jsonData.body();
                   },
                   // Error 처리
                   throwable -> {
                       Log.d(TAG, "onClickPostItem: " + throwable.getMessage());
                   },
                   // Complete 처리
                   () -> {
                       if (post[0] != null) {
                           Pair<View, String> pair1 = Pair.create(image, image.getTransitionName());
                           Pair<View, String> pair2 = Pair.create(like, like.getTransitionName());
                           Pair<View, String> pair3 = Pair.create(comment, comment.getTransitionName());

                           ActivityOptionsCompat options = ActivityOptionsCompat
                                   .makeSceneTransitionAnimation(activity, pair1, pair2, pair3);

                           Log.d(TAG, "onClickPostItem: " + post[0].getAuthor().getProfile_img());
                           intent.putExtra("model", post[0]);
                           v.getContext().startActivity(intent, options.toBundle());
                       }
                   }
               );
    }


    public void onClickUserItem(View v, User model) {
        Intent intent = new Intent(v.getContext(), UserPageView.class);
        intent.putExtra("userid", model.getId());

        if (intent != null)
            v.getContext().startActivity(intent);
    }

    public void onClickedSetting(View v) {
        Intent intent = new Intent(v.getContext(), UserPageView.class);
        intent.putExtra("userid", Const.user.getId());
        v.getContext().startActivity(intent);
    }

    public void onClickedLike(View v, View root, Post model) {
        if (v instanceof ToggleButton) {
            ToggleButton toggleBtn = (ToggleButton)v;

            PostApi.getInstance().pushLike(model.getId(), (code, msg, data) -> {
                model.setNum_liked(((Post) data).getNum_liked());
                Log.d(TAG, "onClickedLike: " + model.getNum_liked());
            });

            if (toggleBtn.isChecked()) {
                toggleBtn.setBackgroundDrawable(
                        root.getResources().
                                getDrawable(R.drawable.icon_like)
                );
            } else {
                toggleBtn.setBackgroundDrawable(
                        root.getResources().
                                getDrawable(R.drawable.icon_unlike)
                );
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onClickedTmp(View v, Activity activity, View image, View like, View comment) {
//        Log.d(TAG, "onClickedTmp: " + activity);

        postAPI = PostApi.getInstance();
        final Post[] post = new Post[1];
        Intent intent = new Intent(v.getContext(), DetailView.class);

        Observable<Response<Post>> postObs = postAPI.getPost("77");
        postObs.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        jsonData -> {
                            if (jsonData.isSuccessful())
                                post[0] = jsonData.body();
                        },
                        // Error 처리
                        throwable -> {},
                        // Complete 처리
                        () -> {
                            if (post[0] != null) {
                                Pair<View, String> pair1 = Pair.create(image, image.getTransitionName());
                                Pair<View, String> pair2 = Pair.create(like, like.getTransitionName());
                                Pair<View, String> pair3 = Pair.create(comment, comment.getTransitionName());

                                ActivityOptionsCompat options = ActivityOptionsCompat
                                        .makeSceneTransitionAnimation(activity, pair1, pair2, pair3);

                                intent.putExtra("model", post[0]);
                                v.getContext().startActivity(intent, options.toBundle());
                            }
                        }
                );
    }

    public void onClickedLogOut(View v, Activity activity) {
        SignUtil.logout(activity, activity);
    }

    public void onClickedClose(View v, DrawerLayout drawerLayout) {
        drawerLayout.closeDrawers();
    }


}
