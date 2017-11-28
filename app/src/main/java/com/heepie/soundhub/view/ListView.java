package com.heepie.soundhub.view;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.heepie.soundhub.R;
import com.heepie.soundhub.databinding.ListViewBinding;
import com.heepie.soundhub.model.User;
import com.heepie.soundhub.viewmodel.ListViewModel;
import com.heepie.soundhub.viewmodel.PostsViewModel;

public class ListView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListViewBinding listBinding = DataBindingUtil.setContentView(this, R.layout.list_view);
        ListViewModel listViewModel = new ListViewModel();

        // 더미 데이터 생성
        for (int i=0; i<10; i=i+1) {
            listViewModel.addPopulUser("populUser " + i, R.drawable.test, "1 " + i);
        }

        for (int i=11; i<20; i=i+1) {
            listViewModel.addPopulPost(
                    new User("populPost " + i, R.drawable.test, "1 " + i),
                    "Title " + i,
                    R.drawable.test,
                    "05:00" + i,
                    "10 " + i,
                    "15 " + i,
                    "#Vocal #Piano" + i
            );
        }

        for (int i=21; i<30; i=i+1) {
            listViewModel.addNewPost(
                    new User("newPost " + i, R.drawable.test, "1 " + i),
                    "Title " + i,
                    R.drawable.test,
                    "05:00" + i,
                    "10 " + i,
                    "15 " + i,
                    "#Vocal #Piano" + i
            );
        }

        listBinding.setViewModel(listViewModel);
        listBinding.setView(this);

    }
}
