package com.heepie.soundhub.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.heepie.soundhub.BuildConfig;
import com.heepie.soundhub.R;
import com.heepie.soundhub.databinding.LoginViewBinding;
import com.heepie.soundhub.domain.logic.UserApi;
import com.heepie.soundhub.domain.model.User;
import com.heepie.soundhub.utils.Const;
import com.heepie.soundhub.utils.RetrofitUtil;
import com.heepie.soundhub.viewmodel.InputViewModel;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;

import java.util.Arrays;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginView extends AppCompatActivity implements InputViewModel.LoginModelListener{
    public final String TAG = getClass().getSimpleName();
    private int GOOGLELOGINREQ = 100;

    InputViewModel inputViewModel = new InputViewModel(this);
    GoogleSignInClient mGoogleSignInClient;
    LoginViewBinding loginViewBinding;
    OAuthLogin mOAuthLoginModule;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginWithStart();
        loginViewBinding = DataBindingUtil.setContentView(this, R.layout.login_view);
        loginViewBinding.setViewModel(inputViewModel);
        setListener();
    }

    private void loginWithNaverStart() {
        mOAuthLoginModule = OAuthLogin.getInstance();
        mOAuthLoginModule.init(this
                ,BuildConfig.NAVER_CLIENT_ID
                ,BuildConfig.NAVER_CLIENT_PW
                ,BuildConfig.NAVER_CLIENT_NAME
        );
        if(null != mOAuthLoginModule.getAccessToken(this)) {
            mOAuthLoginModule.startOauthLoginActivity(this, mOAuthLoginHandler);
        }
    }

    private void loginWithGoogleStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestIdToken(BuildConfig.GOOGLE_CLIENT_ID).build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(null != account) {
            Intent intent = new Intent(this, ListView.class);
            startActivity(intent);
            finish();
        }
    }


    private void loginWithServerStart() {
        SharedPreferences sp = getSharedPreferences("User", Context.MODE_PRIVATE);
        if(!sp.getString("userEmail", "").equals("")) {
            UserApi.IUser service = RetrofitUtil.getInstance().create(UserApi.IUser.class);
            RequestBody email1 = RequestBody.create(MediaType.parse("text/plain"), sp.getString("userEmail", ""));
            RequestBody password1 = RequestBody.create(MediaType.parse("text/plain"), sp.getString("userPass", ""));
            Call<InputViewModel.LoginResult> response = service.getLogin(email1, password1);
            response.enqueue(new Callback<InputViewModel.LoginResult>() {
                @Override
                public void onResponse(Call<InputViewModel.LoginResult> call, Response<InputViewModel.LoginResult> response) {
                    if (response.isSuccessful()) {
                        InputViewModel.LoginResult result = response.body();
                        if(result != null) {
                            Const.TOKEN = result.token;
                            Const.user = result.user;
                            Intent intent = new Intent(LoginView.this, ListView.class);
                            startActivity(intent);
                            finish();
                        }
                    }else {
                        SharedPreferences.Editor editor = sp.edit();
                        editor.clear();
                        editor.commit();
                        Toast.makeText(LoginView.this, "로그인을 다시 시도해 주십시오", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<InputViewModel.LoginResult> call, Throwable t) {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.clear();
                    editor.commit();
                    Toast.makeText(LoginView.this, "로그인을 다시 시도해 주십시오", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void loginWithFacebookStart() {
        callbackManager = CallbackManager.Factory.create();
        if(AccessToken.getCurrentAccessToken() != null) {
            Intent intent = new Intent(this, ListView.class);
            startActivity(intent);
            finish();
        }
    }

    private void loginWithStart() {
        loginWithServerStart();
        loginWithGoogleStart();
        loginWithNaverStart();
        loginWithFacebookStart();
    }

    private void setListener() {
        loginViewBinding.button6.setOnClickListener(view -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, GOOGLELOGINREQ);
        });

        loginViewBinding.button7.setOnClickListener(view -> {
            mOAuthLoginModule.startOauthLoginActivity(this, mOAuthLoginHandler);
        });

        loginViewBinding.button8.setOnClickListener(view -> {
            LoginManager.getInstance().logInWithReadPermissions(this,
                    Arrays.asList("public_profile"));
            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            Log.e("haha", loginResult.getAccessToken() + "");
                            Intent intent = new Intent(LoginView.this, ListView.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onCancel() {
                            Log.e("onCancel", "onCancel");
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            Log.e("onError", "onError " + exception.getLocalizedMessage());
                        }
                    });
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == GOOGLELOGINREQ) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.e("haha", account.getIdToken() + "");
            Intent intent = new Intent(this, ListView.class);
            startActivity(intent);
            finish();
        } catch (ApiException e) {
            Toast.makeText(this, "구글 로그인에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("HandlerLeak")
    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                Intent intent = new Intent(LoginView.this, ListView.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginView.this, "네이버 로그인에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
            }
        };
    };

    private final long FINISH_INTERVAL_TIME = 2000;
    private long   backPressedTime = 0;

    @Override
    public void onBackPressed() {
        if(inputViewModel.isSignUpFiled.get() != 0) {
            inputViewModel.onCancelClicked();
            return;
        } else {
            long tempTime = System.currentTimeMillis();
            long intervalTime = tempTime - backPressedTime;
            String strFlag;

            if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {

                Intent intent = new Intent();
                strFlag = "exit";
                intent.putExtra("value", strFlag);
                setResult(RESULT_OK, intent);
                super.onBackPressed();
            } else {
                backPressedTime = tempTime;
                Toast.makeText(getApplicationContext(), "한번더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void activityClose() {
        finish();
    }
}