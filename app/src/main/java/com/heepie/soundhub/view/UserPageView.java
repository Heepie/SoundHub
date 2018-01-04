package com.heepie.soundhub.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.heepie.soundhub.BuildConfig;
import com.heepie.soundhub.R;
import com.heepie.soundhub.adapter.UserPageTabAdatper;
import com.heepie.soundhub.databinding.UserpageViewBinding;
import com.heepie.soundhub.domain.logic.UserApi;
import com.heepie.soundhub.domain.model.User;
import com.heepie.soundhub.utils.Const;
import com.heepie.soundhub.utils.PathUtil;
import com.heepie.soundhub.utils.RetrofitUtil;
import com.heepie.soundhub.viewmodel.UserPageViewModel;


import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.Result;

/**
 * Created by eunmin on 2017-12-04.
 */

public class UserPageView extends AppCompatActivity {
    User user;
    UserPageViewModel viewModel;
    TabLayout tabLayout;
    UserpageViewBinding binding;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUser();
    }

    public void setListener() {
        tabLayout.addOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager)
        );
        viewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(tabLayout)
        );
    }

    public void setViewPager() {
        viewPager = findViewById(R.id.viewPager);
        UserPageTabAdatper adatper = new UserPageTabAdatper(user, this);
        viewPager.setAdapter(adatper);
    }

    private void setTabLayout() {
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Upload"));
        tabLayout.addTab(tabLayout.newTab().setText("Like"));
        tabLayout.addTab(tabLayout.newTab().setText("Following"));
        tabLayout.addTab(tabLayout.newTab().setText("Follower"));
    }

    private void getUser() {
        Intent intent = getIntent();
        Retrofit retrofit = RetrofitUtil.getInstance();
        UserApi.IUser service = retrofit.create(UserApi.IUser.class);
        Call<User> result = service.get1User(intent.getStringExtra("userid"));
        result.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                binding = DataBindingUtil.setContentView(UserPageView.this, R.layout.userpage_view);
                user = response.body();
                viewModel = new UserPageViewModel(UserPageView.this);
                viewModel.setUserName(user.getNickname());
                viewModel.setUserInstrument(user.getInstrument());
                viewModel.setUserGenre(user.getGenre());
                RequestOptions options = new RequestOptions().centerCrop().placeholder(R.drawable.test2).error(R.drawable.test2).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
                Glide.with(UserPageView.this).load(BuildConfig.MEDIA_URL + user.getProfile_bg()).apply(options).into(binding.frameLayout);
                RequestOptions options1 = new RequestOptions().centerCrop().placeholder(R.drawable.user).error(R.drawable.user).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
                Glide.with(UserPageView.this).load(BuildConfig.MEDIA_URL + user.getProfile_img()).apply(options1).into(binding.userpagepostuimage);
                if (user.getId().equals(Const.user.getId())) {
                    viewModel.setCheck(true);
                }
                binding.setViewModel(viewModel);
                setTabLayout();
                setViewPager();
                setListener();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 22) {
            if(data != null) {
                Uri uri = data.getData();
                String realPath = PathUtil.getPath(this, uri);
                File file = new File(realPath);
                UserApi.IUser service = RetrofitUtil.getInstance().create(UserApi.IUser.class);
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                MultipartBody.Part profile_img = MultipartBody.Part.createFormData("profile_img", file.getName(), requestBody);
                Call<Result> result = service.modifyProimg(Const.user.getId(), "Token " + Const.TOKEN, profile_img);
                result.enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        if(response.isSuccessful()) {
                            binding.userpagepostuimage.setImageURI(uri);
                            Toast.makeText(UserPageView.this, "업로드 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {
                    }
                });
            }
        }else if(requestCode == 33) {
            if(data != null) {
                Uri uri = data.getData();
                String realPath = PathUtil.getPath(this, uri);
                File file = new File(realPath);
                UserApi.IUser service = RetrofitUtil.getInstance().create(UserApi.IUser.class);
                RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
                MultipartBody.Part profile_bg = MultipartBody.Part.createFormData("profile_bg", file.getName(), requestBody);
                Call<Result> result = service.modifyProimg(Const.user.getId(), "Token " + Const.TOKEN, profile_bg);
                result.enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        if(response.isSuccessful()) {
                            binding.frameLayout.setImageURI(uri);
                            Toast.makeText(UserPageView.this, "업로드 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {

                    }
                });
            }
        }
    }

    public void onClickedProfileImg(View view) {
        if(viewModel.check.get()) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Image "), 22);
        }
    }

    public void onClickedBackImg(View view) {
        if(viewModel.check.get()) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Image "), 33);
        }
    }

    @Override
    public void onBackPressed() {
        if(viewModel.sucheck.get()) {
            viewModel.onClickedCancel();
            return;
        }
        super.onBackPressed();
    }
}
