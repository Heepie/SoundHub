package com.heepie.soundhub.view;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.heepie.soundhub.BR;
import com.heepie.soundhub.Interfaces.IGoHome;
import com.heepie.soundhub.R;
import com.heepie.soundhub.databinding.NavigationViewBinding;
import com.heepie.soundhub.databinding.PageViewBinding;
import com.heepie.soundhub.handler.ViewHandler;
import com.heepie.soundhub.listener.EndlessRecyclerOnScrollListener;
import com.heepie.soundhub.utils.Const;
import com.heepie.soundhub.viewmodel.PageViewModel;

public class PageView extends AppCompatActivity implements IGoHome {
    private final String TAG = getClass().getSimpleName();
    private PageViewBinding binding;
    private PageViewModel viewModel;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = PageViewModel.getInstance();
        binding   = DataBindingUtil.setContentView(this, R.layout.page_view);
        binding.setVariable(BR.view, this);
        binding.setVariable(BR.viewModel, viewModel);

        setupWindowAnimations();
        initNavigationView();
        initListener();
        initData();
    }

    public void onClickedMenu(View v) {
        binding.drawerLayout.openDrawer(binding.navigation);
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

    @Override
    public void goHome(View v) {

    }

    private void initData() {
        /*int type = getIntent().getIntExtra("type", -1);
        if (!(type == -1)) {
            viewModel.displayData(type, 0);
        }*/

        // Dummy
        viewModel.displayData(-1, 1);
    }

    private void initListener() {
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {
        // Re-enter transition is executed when returning to this activity
        Slide slideTransition = new Slide();
        slideTransition.setSlideEdge(Gravity.LEFT);
        slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        getWindow().setReenterTransition(slideTransition);
        getWindow().setExitTransition(slideTransition);
    }

    public void onClicked(View v) {
        int page_num = Integer.parseInt(((Button)v).getText().toString());
        viewModel.displayData(-1, page_num);
    }
}
