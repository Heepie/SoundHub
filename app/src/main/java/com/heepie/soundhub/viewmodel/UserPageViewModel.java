package com.heepie.soundhub.viewmodel;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.heepie.soundhub.domain.logic.UserApi;
import com.heepie.soundhub.domain.model.User;
import com.heepie.soundhub.utils.Const;
import com.heepie.soundhub.utils.RetrofitUtil;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by eunmin on 2017-12-04.
 */

public class UserPageViewModel {
    private Context context;
    public final ObservableBoolean check = new ObservableBoolean(false);
    public final ObservableField<String> userName = new ObservableField<>("");
    public final ObservableField<String> userInstrument = new ObservableField<>("");
    public final ObservableField<String> userGenre = new ObservableField<>("");

    public final ObservableBoolean sucheck = new ObservableBoolean(false);

    public UserPageViewModel(Context context) {
        this.context = context;
    }

    public void setCheck(boolean check) {
        this.check.set(check);
    }

    public void setUserName(String userName) {
        this.userName.set(userName);
    }

    public void setUserGenre(String userGenre) {
        this.userGenre.set(userGenre);
    }

    public void setUserInstrument(String userInstrument) {
        this.userInstrument.set(userInstrument);
    }

    public void sucheckChanged(View view) {
        if(sucheck.get()) {
            sucheck.set(false);
        }else {
            sucheck.set(true);
        }
    }

    public void onClickedCancel(View view) {
        userName.set(Const.user.getNickname());
        userInstrument.set(Const.user.getInstrument());
        userGenre.set(Const.user.getGenre());
        sucheckChanged(view);
        onClickedStage(view);
    }

    public void sucheckChanged() {
        if(sucheck.get()) {
            sucheck.set(false);
        }else {
            sucheck.set(true);
        }
    }

    public void onClickedCancel() {
        userName.set(Const.user.getNickname());
        userInstrument.set(Const.user.getInstrument());
        userGenre.set(Const.user.getGenre());
        sucheckChanged();
    }

    public void onClickedModify(View view) {
        UserApi.IUser service = RetrofitUtil.getInstance().create(UserApi.IUser.class);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), userName.get());
        RequestBody instrument = RequestBody.create(MediaType.parse("text/plain"), userInstrument.get());
        RequestBody genre = RequestBody.create(MediaType.parse("text/plain"), userGenre.get());
        Call<User> response = service.getModify(Const.user.getId(), "Token " + Const.TOKEN, name, instrument, genre);
        response.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    sucheck.set(false);
                    Const.user = response.body();
                    userName.set(Const.user.getNickname());
                    userInstrument.set(Const.user.getInstrument());
                    userGenre.set(Const.user.getGenre());
                    Toast.makeText(context, "회원 정보가 수정되었습니다.", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "회원수정에 실패 하셨습니다", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(context, "회원수정에 실패 하셨습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onClickedStage(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
