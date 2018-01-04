package com.heepie.soundhub.viewmodel;

import android.app.Activity;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.heepie.soundhub.Interfaces.SearchDao;
import com.heepie.soundhub.R;
import com.heepie.soundhub.adapter.SearchPullDataAdapter;
import com.heepie.soundhub.adapter.SearchViewTitleAdapter;
import com.heepie.soundhub.databinding.SearchViewBinding;
import com.heepie.soundhub.domain.logic.SearchAPI;
import com.heepie.soundhub.domain.model.Search;
import com.heepie.soundhub.utils.DBHelper;
import com.heepie.soundhub.utils.RetrofitUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by eunmin on 2017-12-18
 */

public class SearchViewModel {

    public ObservableField<String> searchtitle = new ObservableField<>("");
    public ObservableBoolean viewMode = new ObservableBoolean(false);

    public SearchViewTitleAdapter adapter;
    public SearchViewBinding binding;
    public Activity activity;
    public SearchViewModel(SearchViewTitleAdapter adapter, SearchViewBinding binding, Activity activity) {
        this.adapter = adapter;
        this.binding = binding;
        this.activity = activity;
    }

    public void onClickedText(View view) {
        DBHelper dbHelper =  DBHelper.getInstance(view.getContext());
        SearchDao dao = dbHelper.searchDao();
        List<Search> data = dao.getAll();
        adapter.setData(data);
        viewMode.set(true);
    }

    public void onClickedSearch(View view) {
        if(!searchtitle.get().equals("")) {
            DBHelper dbHelper =  DBHelper.getInstance(view.getContext());
            SearchDao dao = dbHelper.searchDao();
            Search model = new Search(searchtitle.get());
            dao.insertItem(model);
            dao.deleteItems();
            SearchAPI.ISearch service = RetrofitUtil.getInstance().create(SearchAPI.ISearch.class);
            Call<SearchAPI.ServerSearch> result = service.result(searchtitle.get());
            result.enqueue(new Callback<SearchAPI.ServerSearch>() {
                @Override
                public void onResponse(Call<SearchAPI.ServerSearch> call, Response<SearchAPI.ServerSearch> response) {
                    if(response.isSuccessful()) {
                        SearchPullDataAdapter adapter = new SearchPullDataAdapter(response.body().getUsers(), response.body().getPosts_by_title(), activity);
                        binding.recyclerView2.setAdapter(adapter);
                        binding.recyclerView2.setLayoutManager(new LinearLayoutManager(view.getContext()));
                    }
                }

                @Override
                public void onFailure(Call<SearchAPI.ServerSearch> call, Throwable t) {

                }
            });
            searchtitle.set("");
            viewMode.set(false);
        }else {
            Toast.makeText(view.getContext(), "검색어를 입력해 주세요.", Toast.LENGTH_SHORT).show();
        }
    }
}