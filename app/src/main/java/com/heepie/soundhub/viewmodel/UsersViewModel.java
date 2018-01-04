package com.heepie.soundhub.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;

import com.heepie.soundhub.Interfaces.IModelGettable;
import com.heepie.soundhub.domain.model.User;

/**
 * Created by Heepie on 2017. 11. 25..
 */

public class UsersViewModel extends BaseObservable
                            implements IModelGettable<ObservableArrayList<UserViewModel>> {
    @Bindable
    public ObservableArrayList<UserViewModel> users;

    public UsersViewModel()
    {
        this.users = new ObservableArrayList<>();
    }

    public void addUserViewModel(UserViewModel viewModel) {
        this.users.add(viewModel);
    }

    @Override
    public ObservableArrayList<UserViewModel> getModel() {
        return users;
    }
}
