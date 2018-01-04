package com.heepie.soundhub.domain.logic;

import com.heepie.soundhub.domain.model.Post;
import com.heepie.soundhub.domain.model.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by eunmin on 2017-12-22.
 */

public class SearchAPI {
    public class ServerSearch {
        public List<User> users = new ArrayList<>();
        public List<Post> posts_by_title = new ArrayList<>();

        public List<User> getUsers() {
            return users;
        }

        public void setUsers(List<User> users) {
            this.users = users;
        }

        public List<Post> getPosts_by_title() {
            return posts_by_title;
        }

        public void setPosts_by_title(List<Post> posts_by_title) {
            this.posts_by_title = posts_by_title;
        }
    }

    public interface ISearch {
        @GET("search")
        Call<ServerSearch> result(@Query("keyword") String keyword);
    }
}
