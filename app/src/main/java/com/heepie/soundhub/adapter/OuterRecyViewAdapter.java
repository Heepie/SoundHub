package com.heepie.soundhub.adapter;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.heepie.soundhub.Interfaces.IModelGettable;
import com.heepie.soundhub.R;
import com.heepie.soundhub.databinding.NewPostViewBinding;
import com.heepie.soundhub.databinding.PopulPostViewBinding;
import com.heepie.soundhub.databinding.PopulUserViewBinding;
import com.heepie.soundhub.handler.ViewHandler;
import com.heepie.soundhub.utils.Const;
import com.heepie.soundhub.viewmodel.ListViewModel;
import com.heepie.soundhub.viewmodel.PostsViewModel;
import com.heepie.soundhub.viewmodel.UsersViewModel;

/**
 * Created by Heepie on 2017. 11. 27..
 * 레이아웃에 의존성이 없는 공통 RecyclerViewAdapter
 */

public class OuterRecyViewAdapter<T extends IModelGettable> extends RecyclerView.Adapter<OuterRecyViewAdapter.Holder> {
    Activity activity;
    ListViewModel mItems;

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setItems(ListViewModel mItems) {
        this.mItems = mItems;
    }

    @Override
    public int getItemViewType(int position) {
        // 1.1 첫번째 위치는 ViewPager로 표현하기 위해 타입 설정
        switch (position) {
            case 0:
                return Const.VIEW_TYPE_POPULAR_USER;
            case 1:
                return Const.VIEW_TYPE_POPULAR_POST;
            case 2:
                return Const.VIEW_TYPE_NEW_POST;
            default:
                return -1;
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
//        Log.d("OuterRecyViewAdapter", "onClickedTmp: Outer" + activity);
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // 각각의 리사이클러 뷰 설정
        switch (viewType) {
            case Const.VIEW_TYPE_POPULAR_USER:
                PopulUserViewBinding populUserViewBinding = DataBindingUtil.inflate(inflater, R.layout.popul_user_view, parent,false);
                populUserViewBinding.setViewModel(mItems.populUsers);
                populUserViewBinding.setActivity(activity);

                Holder<UsersViewModel> usersHolder = new Holder<>(populUserViewBinding, viewType);
                return usersHolder;

            case Const.VIEW_TYPE_POPULAR_POST:
                PopulPostViewBinding populPostViewBinding = DataBindingUtil.inflate(inflater, R.layout.popul_post_view, parent, false);
                populPostViewBinding.setViewModel(mItems.populPosts);
                populPostViewBinding.setActivity(activity);
                populPostViewBinding.setViewhandler(ViewHandler.getIntance());

                Holder<PostsViewModel> populPostsHolder = new Holder<>(populPostViewBinding, viewType);
                return populPostsHolder;

            case Const.VIEW_TYPE_NEW_POST:
                NewPostViewBinding newPostViewBinding = DataBindingUtil.inflate(inflater, R.layout.new_post_view, parent, false);
                newPostViewBinding.setViewModel(mItems.newPosts);
                newPostViewBinding.setActivity(activity);
                newPostViewBinding.setViewhandler(ViewHandler.getIntance());

                Holder<PostsViewModel> newPostsHolder = new Holder<>(newPostViewBinding, viewType);
                return newPostsHolder;

            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        // 1.1 첫번째 위치는 ViewPager로 표현하기 위해 타입 설정
        switch (position) {
            case 0:
                holder.bind(mItems.populUsers);
                break;
            case 1:
                holder.bind(mItems.populPosts);
                break;
            case 2:
                holder.bind(mItems.newPosts);
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (mItems == null)
            return 0;
        else {
            return 3;
        }
    }

    class Holder<E extends IModelGettable> extends RecyclerView.ViewHolder {
        private final ViewDataBinding mBinding;

        private int mViewType;

        public Holder(ViewDataBinding binding, int viewType) {
            super(binding.getRoot());
            mBinding = binding;
            mViewType = viewType;
        }

        public void bind(@NonNull E viewsModel) {

            switch (mViewType) {
                case Const.VIEW_TYPE_POPULAR_USER:
                    ((PopulUserViewBinding)mBinding).setViewModel((UsersViewModel)viewsModel);
                    break;
                case Const.VIEW_TYPE_POPULAR_POST:
                    ((PopulPostViewBinding)mBinding).setViewModel((PostsViewModel)viewsModel);
                    break;
                case Const.VIEW_TYPE_NEW_POST:
                    ((NewPostViewBinding)mBinding).setViewModel((PostsViewModel)viewsModel);
                    break;
            }

            mBinding.executePendingBindings();

        }
    }
}
