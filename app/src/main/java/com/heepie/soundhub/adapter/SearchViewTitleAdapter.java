package com.heepie.soundhub.adapter;

import android.app.Activity;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.heepie.soundhub.Interfaces.SearchDao;
import com.heepie.soundhub.R;
import com.heepie.soundhub.databinding.SearchViewBinding;
import com.heepie.soundhub.domain.logic.SearchAPI;
import com.heepie.soundhub.domain.model.Search;
import com.heepie.soundhub.utils.DBHelper;
import com.heepie.soundhub.utils.RetrofitUtil;
import com.heepie.soundhub.viewmodel.SearchViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by eunmin on 2017-12-18.
 */

public class SearchViewTitleAdapter extends RecyclerView.Adapter<SearchViewTitleAdapter.Holder>{
    List<Search> data = new ArrayList<>();
    Activity activity;
    SearchViewBinding binding;
    SearchViewModel viewModel;

    public SearchViewTitleAdapter(Activity activity, SearchViewBinding binding) {
        this.activity = activity;
        this.binding = binding;
    }
    public void setViewModel(SearchViewModel model) {
        viewModel = model;
    }

    public void setData(List<Search> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_title_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Search model = data.get(position);
        holder.textView.setText(model.getSearch());
        holder.imageView.setOnClickListener(view -> {
            DBHelper dbHelper =  DBHelper.getInstance(view.getContext());
            SearchDao dao = dbHelper.searchDao();
            dao.deleteItem(model);
            setData(dao.getAll());
        });
        holder.stage.setOnClickListener(view -> {
            DBHelper dbHelper =  DBHelper.getInstance(view.getContext());
            SearchDao dao = dbHelper.searchDao();
            Search model2 = new Search(model.getSearch());
            dao.insertItem(model2);
            dao.deleteItems();
            SearchAPI.ISearch service = RetrofitUtil.getInstance().create(SearchAPI.ISearch.class);
            Call<SearchAPI.ServerSearch> result = service.result(model2.getSearch());
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
            viewModel.searchtitle.set("");
            viewModel.viewMode.set(false);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        ConstraintLayout stage;
        public Holder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView26);
            imageView = itemView.findViewById(R.id.imageView9);
            stage = itemView.findViewById(R.id.stage);
        }
    }
}
