package com.heepie.soundhub.domain.model;

import java.util.List;

/**
 * Created by Heepie on 2017. 12. 6..
 */

public class Data {
    private List<User> pop_users;

    private List<Post> pop_posts;

    private List<Post> recent_posts;

    public List<User> getPop_users ()
    {
        return pop_users;
    }

    public void setPop_users (List<User> pop_users)
    {
        this.pop_users = pop_users;
    }

    public List<Post> getPop_posts ()
    {
        return pop_posts;
    }

    public void setPop_posts (List<Post> pop_posts)
    {
        this.pop_posts = pop_posts;
    }

    public List<Post> getRecent_posts ()
    {
        return recent_posts;
    }

    public void setRecent_posts (List<Post> recent_posts)
    {
        this.recent_posts = recent_posts;
    }
}

