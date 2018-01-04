package com.heepie.soundhub.view;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.heepie.soundhub.BR;
import com.heepie.soundhub.Interfaces.IGoHome;
import com.heepie.soundhub.R;
import com.heepie.soundhub.adapter.ExpandListAdapter;
import com.heepie.soundhub.databinding.DetailViewBinding;
import com.heepie.soundhub.databinding.NavigationViewBinding;
import com.heepie.soundhub.domain.model.Post;
import com.heepie.soundhub.handler.ViewHandler;
import com.heepie.soundhub.utils.Const;
import com.heepie.soundhub.viewmodel.DetailViewModel;

import java.util.ArrayList;

public class DetailView extends AppCompatActivity implements IGoHome {
    final String TAG = getClass().getSimpleName();
    private DetailViewModel   viewModel;
    private DetailViewBinding binding;
    private Post model;
    private ExpandListAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding   = DataBindingUtil.setContentView(this, R.layout.detail_view);
        viewModel = DetailViewModel.getInstance();
        viewModel.initContext(this);

        setupWindowAnimations();
        initData();
        initNavigationView();
        initExpandableListView();
        viewModel.setPost(model, binding.expnadListView, adapter);

        binding.setVariable(BR.view, this);
        binding.setVariable(BR.viewModel, viewModel);
    }

    private void initData() {
        model = getIntent().getParcelableExtra("model");
        Log.d(TAG, "initData: " + model.getAuthor().getProfile_img());
        binding.setModel(model);
    }

    private void initNavigationView() {
        NavigationViewBinding naviViewBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.navigation_view, binding.navigation,false);
        binding.navigation.addView(naviViewBinding.getRoot());
        naviViewBinding.setVariable(BR.activity, this);
        naviViewBinding.setVariable(BR.view, this);
        naviViewBinding.setVariable(BR.model, Const.user);
        naviViewBinding.setVariable(BR.viewhandler, ViewHandler.getIntance());
        naviViewBinding.setVariable(BR.drawerLayout, binding.drawerLayout);
    }

    private void initExpandableListView() {
        // Comment의 그룹(피아노, 드럼 등) 추출
        ArrayList<String> groups = new ArrayList<>(model.getComment_tracks().keySet());
        adapter = new ExpandListAdapter(groups, model.getComment_tracks());
        binding.expnadListView.setAdapter(adapter);

        for (int i=0; i<groups.size(); i=i+1)
            binding.expnadListView.expandGroup(i);
    }

    public void onClickedMenu(View v) {
        binding.drawerLayout.openDrawer(binding.navigation);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {
        // We are not interested in defining a new Enter Transition. Instead we change default transition duration
        getWindow().getEnterTransition().setDuration(getResources().getInteger(R.integer.anim_duration_long));
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: ");
        super.onPause();
        viewModel.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
        viewModel.onDestory();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
//        viewModel.setPost(model, binding.expnadListView, adapter);
    }

    @Override
    public void goHome(View v) {
        finish();
    }
}