package com.heepie.soundhub.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
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
import com.heepie.soundhub.domain.model.Post;
import com.heepie.soundhub.view.DetailView;
import com.heepie.soundhub.view.UserPageView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by eunmin on 2017-12-22.
 */

public class SearchPostListAdapter extends RecyclerView.Adapter<SearchPostListAdapter.Holder>{

    List<Post> posts = new ArrayList<>();
    Activity activity;

    public SearchPostListAdapter(List<Post> posts, Activity activity) {
        this.posts = posts;
        this.activity = activity;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_post_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Post post = posts.get(position);
        RequestOptions options1 = new RequestOptions().centerCrop().placeholder(R.drawable.user).error(R.drawable.user).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(holder.itemView.getContext()).load(BuildConfig.MEDIA_URL + post.getAuthor().getProfile_img()).apply(options1).into(holder.userpagepostuimage);
        RequestOptions options2 = new RequestOptions().centerCrop().placeholder(R.drawable.piano).error(R.drawable.piano).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(holder.itemView.getContext()).load(BuildConfig.MEDIA_URL + post.getPost_img()).apply(options2).into(holder.userpagepostpimage);
        holder.userpageposttitle.setText(post.getTitle());
        holder.userpagepostartist.setText(post.getAuthor().getNickname());
        holder.userpagecomments.setText(post.getNum_comments());
        holder.userpageheart.setText(post.getNum_liked());
        holder.userpagepostuimage.setOnClickListener(view -> {
            Intent intent = new Intent(holder.itemView.getContext(), UserPageView.class);
            intent.putExtra("userid", post.getAuthor().getId());
            activity.startActivity(intent);
        });
        holder.stage.setOnClickListener(view -> {
            Intent intent = new Intent(holder.itemView.getContext(), DetailView.class);
            intent.putExtra("model", post);
            activity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        ConstraintLayout stage;
        CircleImageView userpagepostuimage;
        ImageView userpagepostpimage;
        TextView userpageposttitle;
        TextView userpagepostartist;
        TextView userpagecomments;
        TextView userpageheart;
        public Holder(View itemView) {
            super(itemView);
            stage = itemView.findViewById(R.id.stage);
            userpagepostuimage = itemView.findViewById(R.id.userpagepostuimage);
            userpagepostpimage = itemView.findViewById(R.id.userpagepostpimage);
            userpageposttitle = itemView.findViewById(R.id.userpageposttitle);
            userpagepostartist = itemView.findViewById(R.id.userpagepostartist);
            userpagecomments = itemView.findViewById(R.id.userpagecomments);
            userpageheart = itemView.findViewById(R.id.userpageheart);
        }
    }
}
