package com.heepie.soundhub.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.heepie.soundhub.BuildConfig;
import com.heepie.soundhub.domain.logic.UserApi;
import com.heepie.soundhub.domain.model.User;
import com.heepie.soundhub.view.LoginView;
import com.nhn.android.naverlogin.OAuthLogin;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.Result;

/**
 * Created by eunmin on 2017-12-05.
 */

public class SignUtil {
    public static void logout(Context context, Activity activity) {
        SharedPreferences sp = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        if(!sp.getString("userEmail", "").equals("")) {
            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            editor.commit();
            setClear();
            Intent intent = new Intent(context, LoginView.class);
            context.startActivity(intent);
            activity.finishAffinity();
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
        if(null != account) {
            setClear();
            mGoogleSignInClient.signOut();
            Intent intent = new Intent(context, LoginView.class);
            context.startActivity(intent);
            activity.finishAffinity();
        }

        OAuthLogin mOAuthLoginModule = OAuthLogin.getInstance();
        mOAuthLoginModule.init(context
                , BuildConfig.NAVER_CLIENT_ID
                ,BuildConfig.NAVER_CLIENT_PW
                ,BuildConfig.NAVER_CLIENT_NAME
        );
        if(null != mOAuthLoginModule.getAccessToken(context)) {
            mOAuthLoginModule.logout(context);
            setClear();
            Intent intent = new Intent(context, LoginView.class);
            context.startActivity(intent);
            activity.finishAffinity();
        }

        if(AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logOut();
            setClear();
            Intent intent = new Intent(context, LoginView.class);
            context.startActivity(intent);
            activity.finishAffinity();
        }
     }
    private static void setClear() {
        Const.TOKEN = "";
        Const.user = new User();
    }

    public static void logoutAndSignout(Activity activity) {
        SharedPreferences sp = activity.getBaseContext().getSharedPreferences("User", Context.MODE_PRIVATE);
        if(!sp.getString("userEmail", "").equals("")) {
            Retrofit retrofit = RetrofitUtil.getInstance();
            UserApi.IUser service = retrofit.create(UserApi.IUser.class);
            Call<Result> result = service.signOut(Const.user.getId(), "Token " + Const.TOKEN);
            result.enqueue(new Callback<Result>() {
                @Override
                public void onResponse(Call<Result> call, Response<Result> response) {
                    if(response.isSuccessful()) {
                        SharedPreferences.Editor editor = sp.edit();
                        editor.clear();
                        editor.commit();
                        setClear();
                        Intent intent = new Intent(activity.getBaseContext(), LoginView.class);
                        activity.getBaseContext().startActivity(intent);
                        activity.finishAffinity();
                    }
                }

                @Override
                public void onFailure(Call<Result> call, Throwable t) {

                }
            });

        }
    }
}
