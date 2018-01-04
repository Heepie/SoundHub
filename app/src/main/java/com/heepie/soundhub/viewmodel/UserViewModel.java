package com.heepie.soundhub.viewmodel;

import android.databinding.BaseObservable;

import com.heepie.soundhub.Interfaces.IModelGettable;
import com.heepie.soundhub.Interfaces.IShowable;
import com.heepie.soundhub.domain.model.User;

/**
 * Created by Heepie on 2017. 11. 25..
 */

public class UserViewModel extends BaseObservable implements IModelGettable<User> , IShowable{
    private User model;

    public UserViewModel(User model) {
        this.model = model;
    }

    @Override
    public User getModel() {
        return model;
    }

    @Override
    public boolean isShow() {
        return true;
    }
}
