package com.heepie.soundhub.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.heepie.soundhub.BuildConfig;
import com.heepie.soundhub.R;
import com.heepie.soundhub.domain.model.User;
import com.heepie.soundhub.utils.Const;
import com.heepie.soundhub.view.UserPageView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by eunmin on 2017-12-22.
 */

public class SearchUserListAdpater extends RecyclerView.Adapter<SearchUserListAdpater.Holder>{

    List<User> users = new ArrayList<>();
    Activity activity;

    public SearchUserListAdpater(List<User> users, Activity activity) {
        this.users = users;
        this.activity = activity;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_user_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        User user = users.get(position);
        RequestOptions options1 = new RequestOptions().centerCrop().placeholder(R.drawable.user).error(R.drawable.user).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(activity).load(BuildConfig.MEDIA_URL + user.getProfile_img()).apply(options1).into(holder.userpagepostuimage);
        holder.textView14.setText(user.getNickname());
        holder.constraintLayout.setOnClickListener(view -> {
            Intent intent = new Intent(holder.itemView.getContext(), UserPageView.class);
            intent.putExtra("userid", user.getId());
            activity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        CircleImageView userpagepostuimage;
        TextView textView14;
        ConstraintLayout constraintLayout;
        public Holder(View itemView) {
            super(itemView);
            userpagepostuimage = itemView.findViewById(R.id.userpagepostuimage);
            textView14 = itemView.findViewById(R.id.textView14);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);
        }
    }
}
