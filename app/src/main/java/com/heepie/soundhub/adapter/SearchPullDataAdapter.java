package com.heepie.soundhub.adapter;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.heepie.soundhub.R;
import com.heepie.soundhub.domain.model.Post;
import com.heepie.soundhub.domain.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eunmin on 2017-12-22.
 */

public class SearchPullDataAdapter extends RecyclerView.Adapter<SearchPullDataAdapter.Holder>{

    public List<User> users = new ArrayList<>();
    public List<Post> posts_by_title = new ArrayList<>();
    Activity activity;

    public SearchPullDataAdapter() {

    }

    public SearchPullDataAdapter(List<User> users, List<Post> posts_by_title, Activity activity) {
        this.users = users;
        this.posts_by_title = posts_by_title;
        this.activity = activity;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return 0;
            case 1:
                return 1;
            default:
                return -1;
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if(viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_user_list, parent, false);
        }else if(viewType == 1){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_post_list, parent, false);
        }
        return new Holder(view, viewType);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 2;
    }

    class Holder extends RecyclerView.ViewHolder {

        public Holder(View itemView, int viewType) {
            super(itemView);
            if(viewType == 0) {
                if(users.size() != 0) {
                    RecyclerView recyclerView = itemView.findViewById(R.id.recyclerView);
                    SearchUserListAdpater userListAdpater = new SearchUserListAdpater(users, activity);
                    recyclerView.setAdapter(userListAdpater);
                    recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
                }
            }else if(viewType == 1){
                if(posts_by_title.size() != 0) {
                    RecyclerView recyclerView = itemView.findViewById(R.id.recyclerView);
                    SearchPostListAdapter postListAdapter = new SearchPostListAdapter(posts_by_title, activity);
                    recyclerView.setAdapter(postListAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
                }
            }
        }
    }
}
