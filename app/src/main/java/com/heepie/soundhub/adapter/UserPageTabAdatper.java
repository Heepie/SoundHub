package com.heepie.soundhub.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.heepie.soundhub.R;
import com.heepie.soundhub.domain.model.Post;
import com.heepie.soundhub.domain.model.User;
import com.heepie.soundhub.domain.model.User_Post;

import java.util.ArrayList;
import java.util.List;

public class UserPageTabAdatper extends PagerAdapter{
    User user;
    Activity activity;
    public UserPageTabAdatper() {

    }

    public UserPageTabAdatper(User user, Activity activity) {
        this.user = user;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.userpage_recycler, null);
        RecyclerView recyclerView = view.findViewById(R.id.userpagerecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));


        if(position == 0) {
            List<User_Post> uploadData = new ArrayList<>();
            if(null != user.getPost_set()) {
                for (User_Post post : user.getPost_set()) {
                    uploadData.add(post);
                }
            }
            UserPageMyPostListAdapter adapter = new UserPageMyPostListAdapter(uploadData, activity);
            recyclerView.setAdapter(adapter);
        }else if(position == 1) {
            List<User_Post> likedData = new ArrayList<>();
            if(null != user.getLiked_posts()) {
                for (User_Post post : user.getLiked_posts()) {
                    likedData.add(post);
                }
            }
            UserPagePostListAdapter adapter = new UserPagePostListAdapter(likedData, activity);
            recyclerView.setAdapter(adapter);
        }
        else if(position == 2) {
            List<String> following = new ArrayList<>();
            if(user.getFollowing().size() != 0) {
                for (String string : user.getFollowing()) {
                    following.add(string);
                }
            }
            UserPageUserListAdapter adapter = new UserPageUserListAdapter(following, activity);
            recyclerView.setAdapter(adapter);
        }else if(position == 3) {
            List<String> follower = new ArrayList<>();
            if(user.getFollowers().size() != 0) {
                for (String string : user.getFollowers()) {
                    follower.add(string);
                }
            }
            UserPageUserListAdapter adapter = new UserPageUserListAdapter(follower, activity);
            recyclerView.setAdapter(adapter);
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
