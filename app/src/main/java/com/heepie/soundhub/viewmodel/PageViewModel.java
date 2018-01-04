package com.heepie.soundhub.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;

import com.heepie.soundhub.domain.logic.PostApi;
import com.heepie.soundhub.domain.model.Post;
import com.heepie.soundhub.domain.model.User;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;

/**
 * Created by Heepie on 2017. 12. 18..
 */

public class PageViewModel extends BaseObservable {
    private final String TAG = getClass().getSimpleName();
    public static PageViewModel instance;
    private PostApi postApi;
    private List<Post> postList;

    // For Dummy
    private int index;
    @Bindable
    public PostsViewModel postsViewModel;

    private PageViewModel() {
        index=0;
        postApi = PostApi.getInstance();
        postList = new ArrayList<>();
        postsViewModel = new PostsViewModel();
    }

    public static PageViewModel getInstance() {
        if (instance == null)
            instance = new PageViewModel();
        return instance;
    }

    public void displayData(int type, int page_num) {
        // TODO 서버 API 완료 후 진행
        /*Observable<Response<List<Post>>> postObs = postApi.getPosts(type, page_num);
        postObs.observeOn(Schedulers.io())
               .subscribeOn(Schedulers.newThread())
               .subscribe(
                       jsonData -> {
                           postList = jsonData.body();
                           setData(postList);
                       }
               );*/

        // Dummy Data
        postsViewModel.posts.clear();
        int startIndex = (page_num-1)*100;
        Log.d(TAG, "displayData: before" + index);
        for (int i=startIndex; i<startIndex+100; i=i+1) {
            Post dummyPost = new Post();
            dummyPost.setId(i+"");
            User user = new User();
            user.setId(""+i);
            dummyPost.setAuthor(user);
            dummyPost.setNum_liked(i+"");
            dummyPost.setNum_comments(i+"");
            postsViewModel.addPostViewModel(new PostViewModel(dummyPost));
        }
        Log.d(TAG, "displayData: after" + index);
    }

    private void setData(List<Post> postList) {
        for (Post post : postList) {
            postsViewModel.addPostViewModel(new PostViewModel(post));
        }
    }
}
