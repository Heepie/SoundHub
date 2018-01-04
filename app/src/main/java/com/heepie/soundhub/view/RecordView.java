package com.heepie.soundhub.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.heepie.soundhub.BR;
import com.heepie.soundhub.R;
import com.heepie.soundhub.databinding.RecordViewBinding;
import com.heepie.soundhub.utils.Const;
import com.heepie.soundhub.utils.PathUtil;

import java.io.File;

public class RecordView extends AppCompatActivity {
    public final String TAG = getClass().getSimpleName();
    public int[] layoutResIds;
    public ObservableField<String> mFileName;
    private RecordViewBinding binding;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.record_view);
        binding.setVariable(BR.view, this);

        initTabLayout();
        connectViewpagerAndTabLayout();
        initData();
    }

    private void initTabLayout() {
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("녹음후 업로드"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("파일로 업로드"));
    }

    private void initData() {
        mFileName = new ObservableField<>(" ");

        layoutResIds = new int[2];
        layoutResIds[0] = R.layout.chlid_record_view;
        layoutResIds[1] = R.layout.chlid_file_upload_view;
    }

    private void connectViewpagerAndTabLayout() {
        binding.tabLayout.addOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(binding.viewPager)
        );

        binding.viewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout)
        );
    }

    public void onClickedFileUpload(View v) {
        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Audio "), Const.SELECT_AUDIO_TRACK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String realPath="";
        if (requestCode == Const.SELECT_AUDIO_TRACK) {
            Uri uri = data.getData();
            realPath = PathUtil.getPath(this, uri);
            Log.d(TAG, "onActivityResult: " + realPath);
            mFileName.set(realPath);
        }
    }
}
