package com.heepie.soundhub.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.heepie.soundhub.BR;
import com.heepie.soundhub.Interfaces.ICallback;
import com.heepie.soundhub.Interfaces.IGoHome;
import com.heepie.soundhub.R;
import com.heepie.soundhub.databinding.ListViewBinding;
import com.heepie.soundhub.databinding.NavigationViewBinding;
import com.heepie.soundhub.handler.ViewHandler;
import com.heepie.soundhub.utils.Const;
import com.heepie.soundhub.viewmodel.ListViewModel;

import java.io.File;

public class ListView extends AppCompatActivity implements IGoHome {
    public final String TAG = getClass().getSimpleName();
    private String          category;
    private ListViewModel   listViewModel;
    private ListViewBinding listBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Binding 초기화
        listBinding = DataBindingUtil.setContentView(this, R.layout.list_view);
        listViewModel = new ListViewModel(this);

        initNavigationView();
        initData(Const.CATEGORY_DEFAULT, (code, msg, data) -> {
            if  (code == Const.RESULT_SUCCESS)
                listBinding.progressBar.setVisibility(View.GONE);
        });

        listBinding.setVariable(BR.viewModel, listViewModel);
        listBinding.setVariable(BR.view, this);
    }

    private void initData(String category, ICallback callback) {
        listViewModel.resetData();
        listViewModel.setDisplayData(category, callback);
        listViewModel.setDisplayCategory();
    }

    private void initNavigationView() {
        NavigationViewBinding naviViewBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.navigation_view,listBinding.navigation,false);
        listBinding.navigation.addView(naviViewBinding.getRoot());
        naviViewBinding.setVariable(BR.activity, this);
        naviViewBinding.setVariable(BR.view, this);
        naviViewBinding.setVariable(BR.model, Const.user);
        naviViewBinding.setVariable(BR.viewhandler, ViewHandler.getIntance());
        naviViewBinding.setVariable(BR.drawerLayout, listBinding.drawerLayout);
    }

    public void onClickedTopCategory(View v) {
        category = ((Button) v).getText().toString().toLowerCase();
        switch (v.getId()) {
            case R.id.category_genre:
                if (listBinding.genreCategory.getVisibility() == View.VISIBLE) {
                    listBinding.genreCategory.setVisibility(View.GONE);
                    listBinding.instrumentCategory.setVisibility(View.GONE);
                } else {
                    listBinding.genreCategory.setVisibility(View.VISIBLE);
                    listBinding.instrumentCategory.setVisibility(View.GONE);
                }
                break;
            case R.id.category_instrument:
                if (listBinding.instrumentCategory.getVisibility() == View.VISIBLE) {
                    listBinding.genreCategory.setVisibility(View.GONE);
                    listBinding.instrumentCategory.setVisibility(View.GONE);
                } else {
                    listBinding.genreCategory.setVisibility(View.GONE);
                    listBinding.instrumentCategory.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    public void onClickedCategory(View v) {
        listBinding.genreCategory.setVisibility(View.GONE);
        listBinding.instrumentCategory.setVisibility(View.GONE);
        initData(category + File.separator + ((Button) v).getText().toString(),
                (code, msg, data) -> {
                    if (code == Const.RESULT_SUCCESS)
                        listBinding.progressBar.setVisibility(View.GONE);
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void onClickedMenu(View v) {
        listBinding.drawerLayout.openDrawer(listBinding.navigation);
    }

    // 서버로 부터 카테고리 정보를 입력 받는 메소드
    private void setCategoryData() {
        // 1. 서버와 통신 후 카테고리 정보 가져오기

        // 2. 해당 카테고리 설정
        // 2-1. 해당 이름에 따라 Button 카테고리 설정 및 빈 카테고리 버튼은 View.GONE 처리
    }

    @Override
    public void goHome(View v) {
        ViewHandler.getIntance().initIndex();
        refreshHome();
    }

    private void refreshHome() {
        listBinding.drawerLayout.closeDrawers();
        listBinding.genreCategory.setVisibility(View.GONE);
        listBinding.instrumentCategory.setVisibility(View.GONE);
        listBinding.progressBar.setVisibility(View.VISIBLE);
        initData(Const.CATEGORY_DEFAULT,
                (code, msg, data) -> {
                    if (code == Const.RESULT_SUCCESS)
                        listBinding.progressBar.setVisibility(View.GONE);
                });
    }

    public void goSearch(View v) {
        Intent intent = new Intent(this, SearchView.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
//        refreshHome();
        super.onResume();
    }
}