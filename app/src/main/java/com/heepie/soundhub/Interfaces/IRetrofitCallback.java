package com.heepie.soundhub.Interfaces;

import com.heepie.soundhub.domain.model.Post;

import java.util.List;

/**
 * Created by Heepie on 2017. 11. 30..
 */

public interface IRetrofitCallback {
    void initData(int code, String msg, List<Post> data);
}
