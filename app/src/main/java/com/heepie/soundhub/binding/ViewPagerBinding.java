package com.heepie.soundhub.binding;

import android.app.Activity;
import android.databinding.BindingAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.heepie.soundhub.R;
import com.heepie.soundhub.adapter.ViewPagerAdapter;

/**
 * Created by Heepie on 2017. 12. 11..
 */

public class ViewPagerBinding {
    @BindingAdapter({"setViewPagerLayout", "setActivity"})
    public static void setViewPagerLayout(View view, int[] layoutResIds, Activity activity) {
        if (view instanceof ViewPager) {
            Log.d("ViewPagerBinding", "setViewPagerLayout: " + "a");
            ViewPagerAdapter adapter = new ViewPagerAdapter(view.getContext(), layoutResIds, activity);
            ((ViewPager)view).setAdapter(adapter);
        }
    }
}
