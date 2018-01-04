package com.heepie.soundhub.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.adapters.TextViewBindingAdapter;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.heepie.soundhub.domain.logic.UserApi;
import com.heepie.soundhub.domain.model.Post;
import com.heepie.soundhub.domain.model.User;
import com.heepie.soundhub.utils.Const;
import com.heepie.soundhub.utils.RetrofitUtil;
import com.heepie.soundhub.view.ListView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.Result;


/**
 * Created by Heepie on 2017. 11. 27..
 */

public class InputViewModel {

    public interface LoginModelListener {
        public void activityClose();
    }

    public InputViewModel(LoginModelListener listener) {
        this.listener = listener;
    }

    public LoginModelListener listener;

    public ObservableField<String> email = new ObservableField<>("");
    public ObservableField<String> password = new ObservableField<>("");
    public ObservableField<String> passwordCheck = new ObservableField<>("");
    public ObservableField<String> genre = new ObservableField<>("");
    public ObservableField<String> instrument = new ObservableField<>("");
    public ObservableField<String> nickname = new ObservableField<>("");

    public ObservableField<Integer> isSignUpFiled = new ObservableField<>(0);

    public ObservableBoolean isEmailCheck = new ObservableBoolean(false);
    public ObservableBoolean isPasswordCheck = new ObservableBoolean(false);
    public ObservableBoolean isPassword1Check = new ObservableBoolean(false);

    public final ObservableField<Boolean> showSignUp = new ObservableField<>(false);
    public final ObservableField<Boolean> showSignIn = new ObservableField<>(false);

    public void showSignUp(View view) {
        isSignUpFiled.set(2);
    }

    public void showSignIn(View view) {
        isSignUpFiled.set(1);
    }

    public void onEmailChanged(CharSequence s, int start, int before, int count) {
        isEmailCheck.set(isValidEmail());
    }

    public void onPasswordChanged(CharSequence s, int start, int before, int count) {
        isPasswordCheck.set(isValidPassword());
        isPassword1Check.set(password.get().equals(passwordCheck.get()));
    }

    public void onPassword1Changed(CharSequence s, int start, int before, int count) {
        isPasswordCheck.set(isValidPassword());
        isPassword1Check.set(password.get().equals(s.toString()));
    }

    public void onSignUpClicked(View view) {
        email.set("");
        password.set("");
        isSignUpFiled.set(2);
    }

    public void onCancelClicked(View view) {
        email.set("");
        password.set("");
        passwordCheck.set("");
        genre.set("");
        isSignUpFiled.set(0);
    }
    public void onCancelClicked() {
        email.set("");
        password.set("");
        passwordCheck.set("");
        genre.set("");
        isSignUpFiled.set(0);
    }

    public void onContinueClicked(View view) {
        isSignUpFiled.set(3);
    }

    public void onLoginClicked(View view) {
        Retrofit retrofit = RetrofitUtil.getInstance();
        UserApi.IUser service = retrofit.create(UserApi.IUser.class);
        RequestBody email1 = RequestBody.create(MediaType.parse("text/plain"), email.get());
        RequestBody password1 = RequestBody.create(MediaType.parse("text/plain"), password.get());
        Call<LoginResult> response = service.getLogin(email1, password1);

        response.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                if(response.isSuccessful()) {
                    LoginResult result = response.body();
                    Const.TOKEN = result.token;
                    Const.user = result.user;
                    SharedPreferences sp = view.getContext().getSharedPreferences("User", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("userEmail", email.get());
                    editor.putString("userPass", password.get());
                    editor.commit();
                    Intent intent = new Intent(view.getContext(), ListView.class);
                    view.getContext().startActivity(intent);
                    listener.activityClose();
                }else {
                    Toast.makeText(view.getContext(), "이메일과 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                Toast.makeText(view.getContext(), "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onClickedSignUp(View view) {
        Retrofit retrofit = RetrofitUtil.getInstance();
        UserApi.IUser service = retrofit.create(UserApi.IUser.class);
        RequestBody email1 = RequestBody.create(MediaType.parse("text/plain"), email.get());
        RequestBody password1 = RequestBody.create(MediaType.parse("text/plain"), password.get());
        RequestBody instrument1 = RequestBody.create(MediaType.parse("text/plain"), instrument.get());
        RequestBody nickname1 = RequestBody.create(MediaType.parse("text/plain"), nickname.get());
        RequestBody genre1 = RequestBody.create(MediaType.parse("text/plain"), genre.get());
        Call<Result> result = service.signUpUser(email1, password1, password1, instrument1, nickname1);
        result.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                onCancelClicked();
                Toast.makeText(view.getContext(), "이메일을 확인해 주세요..", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(view.getContext(), "회원가입에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class LoginResult {
        public String token;
        public User user;
    }

    public boolean isValidPassword() {
        String regex = "^[A-Za-z0-9]{6,16}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(password.get());
        return m.matches();
    }

    public boolean isValidEmail() {
        String regex = "^[a-z0-9A-Z_-]+(.[a-z0-9A-Z_-])*@([a-z0-9A-Z.])+.([a-zA-Z]){2,}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email.get());
        return m.matches();
    }
//
//    public boolean isValidName(){
//        String regex = "^[가-힣A-Za-z0-9]{2,12}$";
//        Pattern p = Pattern.compile(regex);
//        Matcher m = p.matcher(name.get());
//        return m.matches();
//    }


}
