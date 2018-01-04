package com.heepie.soundhub.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.heepie.soundhub.BuildConfig;
import com.heepie.soundhub.R;
import com.heepie.soundhub.domain.logic.PostApi;
import com.heepie.soundhub.domain.model.Post;
import com.heepie.soundhub.domain.model.User_Post;
import com.heepie.soundhub.utils.Const;
import com.heepie.soundhub.view.DetailView;
import com.heepie.soundhub.view.UserPageView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Created by eunmin on 2017-12-11.
 */

public class UserPagePostListAdapter extends RecyclerView.Adapter<UserPagePostListAdapter.Holder>{
    List<User_Post> data = new ArrayList<>();
    Activity activity;

    public UserPagePostListAdapter(List<User_Post> data, Activity activity) {
        this.data = data;
        this.activity = activity;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.userpage_post_list_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        PostApi service = PostApi.getInstance();
        final Post[] post = {new Post()};
        Observable<Response<Post>> jsonpost = service.getPost(data.get(position).getId());
        jsonpost.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(jasonData -> {
            post[0] = jasonData.body();
        }, throwable -> {

        }, () -> {
            holder.textTitle.setText(post[0].getTitle() + "");
            holder.textHeart.setText(post[0].getNum_liked() + "");
            holder.textComments.setText(post[0].getNum_comments() + "");
            holder.textArtist.setText(post[0].getAuthor().getNickname());
            RequestOptions options1 = new RequestOptions().centerCrop().placeholder(R.drawable.user).error(R.drawable.user).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
            Glide.with(holder.itemView.getContext()).load(BuildConfig.MEDIA_URL + post[0].getAuthor().getProfile_img()).apply(options1).into(holder.userpagepostuimage);
            RequestOptions options2 = new RequestOptions().centerCrop().placeholder(R.drawable.piano).error(R.drawable.piano).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
            Glide.with(holder.itemView.getContext()).load(BuildConfig.MEDIA_URL + post[0].getPost_img()).apply(options2).into(holder.userpagepostpimage);
            holder.userpagepostuimage.setOnClickListener(view -> {
                Intent intent = new Intent(holder.itemView.getContext(), UserPageView.class);
                intent.putExtra("userid", post[0].getAuthor().getId());
                activity.startActivity(intent);
            });
            holder.stage.setOnClickListener(view -> {
                Intent intent = new Intent(holder.itemView.getContext(), DetailView.class);
                intent.putExtra("model", post[0]);
                view.getContext().startActivity(intent);
            });
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class Holder extends RecyclerView.ViewHolder{

        TextView textTitle;
        TextView textArtist;
        TextView textHeart;
        TextView textComments;
        CircleImageView userpagepostuimage;
        ImageView userpagepostpimage;
        ConstraintLayout stage;

        public Holder(View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.userpageposttitle);
            textArtist = itemView.findViewById(R.id.userpagepostartist);
            textHeart = itemView.findViewById(R.id.userpageheart);
            textComments = itemView.findViewById(R.id.userpagecomments);
            userpagepostuimage = itemView.findViewById(R.id.userpagepostuimage);
            userpagepostpimage = itemView.findViewById(R.id.userpagepostpimage);
            stage = itemView.findViewById(R.id.stage);
        }
    }
}
