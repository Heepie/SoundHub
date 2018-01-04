package com.heepie.soundhub.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.heepie.soundhub.R;
import com.heepie.soundhub.databinding.MusicuploadViewBinding;
import com.heepie.soundhub.utils.PathUtil;
import com.heepie.soundhub.utils.SignUtil;
import com.heepie.soundhub.viewmodel.MusicUploadModel;

import java.io.File;

/**
 * Created by eunmin on 2017-12-04.
 */

public class MusicUploadView extends AppCompatActivity implements MusicUploadModel.MusicUpload{
    MusicUploadModel model;
    MusicuploadViewBinding binding;
    String realPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.musicupload_view);
        model = new MusicUploadModel(this, this, binding);
        binding.setViewModel(model);
        binding.button4.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("audio/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Audio "), 22);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 22) {
            if(data != null) {
                Uri uri = data.getData();
                realPath = PathUtil.getPath(this, uri);
                File file = new File(realPath);
                model.file_name.set(file.getName());
            }
        }
    }

    @Override
    public String setModel() {
        return realPath;
    }

    @Override
    public void finishActivityss() {
        finish();
    }

}
