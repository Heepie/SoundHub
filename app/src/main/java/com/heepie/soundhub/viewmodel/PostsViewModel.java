package com.heepie.soundhub.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableArrayMap;
import android.databinding.ObservableField;
import android.view.View;

import com.heepie.soundhub.Interfaces.IModelGettable;
import com.heepie.soundhub.R;
import com.heepie.soundhub.domain.model.Post;
import com.heepie.soundhub.domain.model.User;

/**
 * Created by Heepie on 2017. 11. 24..
 * View Model은 View를 참조하지 않는다. 오직 비즈니스 로직(데이터를 표현하는 방법만 집중)
 * ViewModel은 생산자, View는 소비자
 * 소비자는 생산자가 누구인지 알아야하지만, 생산자는 소비자가 누구인지 몰라도 된다.
 */

public class PostsViewModel extends BaseObservable
                            implements IModelGettable<ObservableArrayList<PostViewModel>>
{
    @Bindable
    public ObservableArrayList<PostViewModel> posts;

    public PostsViewModel()
    {
        posts = new ObservableArrayList<>();
        // 초기 데이터 설정 필요
    }

    public void addPostViewModel(PostViewModel viewModel) {
        this.posts.add(viewModel);
    }

    public ObservableArrayList<PostViewModel> getModel() {
        return posts;
    }

    public void setPosts(ObservableArrayList<PostViewModel> posts) {
        this.posts = posts;
    }
}
