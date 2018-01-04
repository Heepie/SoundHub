package com.heepie.soundhub.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.heepie.soundhub.BuildConfig;
import com.heepie.soundhub.R;
import com.heepie.soundhub.domain.logic.UserApi;
import com.heepie.soundhub.domain.model.User;
import com.heepie.soundhub.utils.Const;
import com.heepie.soundhub.utils.RetrofitUtil;
import com.heepie.soundhub.view.UserPageView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by eunmin on 2017-12-11.
 */

public class UserPageUserListAdapter extends RecyclerView.Adapter<UserPageUserListAdapter.Holder>{
    List<String> data = new ArrayList<>();
    Activity activity;

    public UserPageUserListAdapter(List<String> data, Activity activity) {
        this.data = data;
        this.activity = activity;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.userpage_user_list_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        UserApi.IUser service = RetrofitUtil.getInstance().create(UserApi.IUser.class);
        Call<User> result = service.get1User(data.get(position));
        result.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                holder.textView.setText(response.body().getNickname());
                String a = BuildConfig.MEDIA_URL + response.body().getProfile_img();
                RequestOptions options = new RequestOptions().centerCrop().placeholder(R.drawable.user).error(R.drawable.user);
                Glide.with(activity).load(a).apply(options).into(holder.userpagepostuimage);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
        holder.constraintLayout.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), UserPageView.class);
            intent.putExtra("userid", data.get(position));
            view.getContext().startActivity(intent);
            activity.finish();
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        CircleImageView userpagepostuimage;
        ConstraintLayout constraintLayout;
        TextView textView;
        public Holder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView14);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);
            userpagepostuimage = itemView.findViewById(R.id.userpagepostuimage);
        }
    }
}