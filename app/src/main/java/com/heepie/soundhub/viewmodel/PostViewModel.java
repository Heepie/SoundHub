package com.heepie.soundhub.viewmodel;

import android.databinding.BaseObservable;

import com.heepie.soundhub.Interfaces.IModelGettable;
import com.heepie.soundhub.Interfaces.IShowable;
import com.heepie.soundhub.domain.model.Post;


/**
 * Created by Heepie on 2017. 11. 24..
 */

public class PostViewModel extends BaseObservable implements IModelGettable<Post>, IShowable {
    private Post model;

    public PostViewModel(Post model) {
        this.model = model;
    }

    @Override
    public Post getModel() {
        return model;
    }

    public void setModel(Post model) {
        this.model = model;
    }

    @Override
    public boolean isShow() {
        return true;
//        return model.getIsShow();
    }
}