package com.heepie.soundhub.adapter;

import android.app.Activity;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.heepie.soundhub.BR;
import com.heepie.soundhub.handler.ViewHandler;
import com.heepie.soundhub.utils.Const;
import com.heepie.soundhub.view.DetailView;
import com.heepie.soundhub.view.RecordView;
import com.heepie.soundhub.viewmodel.DetailViewModel;

/**
 * Created by Heepie on 2017. 12. 11..
 */

public class ViewPagerAdapter extends PagerAdapter {
    private Context context;
    private int[] layoutResIds;
    private Activity activity;

    public ViewPagerAdapter(Context context, int[] layoutResIds, Activity activity) {
        this.context = context;
        this.layoutResIds = layoutResIds;
        this.activity = activity;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, layoutResIds[position], container, false);
        binding.setVariable(BR.viewHandler, ViewHandler.getIntance());
        binding.setVariable(BR.viewModel, DetailViewModel.getInstance());
        binding.setVariable(BR.view, activity);

        container.addView(binding.getRoot());

        return binding.getRoot();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
}
