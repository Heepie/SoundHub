package com.heepie.soundhub.adapter;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.heepie.soundhub.BR;
import com.heepie.soundhub.Interfaces.IModelGettable;
import com.heepie.soundhub.Interfaces.IShowable;
import com.heepie.soundhub.domain.model.User;
import com.heepie.soundhub.handler.ViewHandler;

/**
 * Created by Heepie on 2017. 11. 28..
 */

public class InnerRecyclerAdapter<T extends IModelGettable & IShowable> extends RecyclerView.Adapter<InnerRecyclerAdapter.Holder> {
    int layoutResId;
    Activity activity;
    ObservableArrayList<T> mItems;

    public InnerRecyclerAdapter() {

    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setItems(ObservableArrayList<T> mItems, int layoutResId) {
        this.mItems = mItems;
        this.layoutResId = layoutResId;
    }



    @Override
    public InnerRecyclerAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, layoutResId, parent,false);
        InnerRecyclerAdapter.Holder holder = new InnerRecyclerAdapter.Holder(binding);

        return holder;
    }

    @Override
    public void onBindViewHolder(InnerRecyclerAdapter.Holder holder, int position) {
        T model = (T)mItems.get(position);
        holder.bind(mItems.get(position).getModel());
    }

    @Override
    public int getItemCount() {
        if (mItems == null)
            return 0;

        return mItems.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private ViewDataBinding mBinding;

        public Holder(ViewDataBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(@NonNull Object obj) {
//            Log.d("InnerRecyclerAdapter", "onClickedTmp: Inner" + activity);
            mBinding.setVariable(BR.activity, activity);
            mBinding.setVariable(BR.view, mBinding.getRoot());
            mBinding.setVariable(BR.model, obj);
            mBinding.setVariable(BR.viewhandler, ViewHandler.getIntance());
            mBinding.executePendingBindings();
        }
    }
}
