package com.heepie.soundhub.binding;

import android.databinding.BindingAdapter;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.heepie.soundhub.adapter.ExpandListAdapter;
import com.heepie.soundhub.domain.model.Post;

import java.util.ArrayList;

/**
 * Created by Heepie on 2017. 12. 6..
 */

public class ExpandViewBinding {

    @BindingAdapter("setExModel")
    public static void setViewModel(View view, Post model) {
        if (view instanceof ExpandableListView) {
            // Comment의 그룹(피아노, 드럼 등) 추출
            ArrayList<String> groups = new ArrayList<>();
            groups.add("Mixed");
            groups.addAll(model.getComment_tracks().keySet());
            Log.d("ExpandViewBinding", "setViewModel: " + groups.toString());
            ExpandListAdapter adapter = new ExpandListAdapter(groups, model.getComment_tracks());
            ((ExpandableListView) view).setAdapter(adapter);

            for (int i = 0; i < groups.size(); i = i + 1)
                ((ExpandableListView) view).expandGroup(i);
        }
    }
}
