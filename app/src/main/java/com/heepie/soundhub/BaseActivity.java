package com.heepie.soundhub;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.facebook.stetho.Stetho;
import com.heepie.soundhub.view.LoginView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eunmin on 2017-11-30.
 */

public class BaseActivity extends AppCompatActivity {

    private static final int REQ_CODE = 999;
    private static final String permissions[] = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        // 앱 버전 체크 - 호환성 처리
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkPermission();
        }else{
            Intent intent = new Intent(this, LoginView.class);
            startActivity(intent);
            finish();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermission(){
        // 권한에 대한 승인 여부
        List<String> requires = new ArrayList<>();
        for(String permission : permissions){
            if(checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED){
                requires.add(permission);
            }
        }
        // 승인이 안된 권한이 있을 경우만 승인 요청
        if(requires.size() > 0){
            String perms[] = requires.toArray(new String[requires.size()]);
            requestPermissions(perms, REQ_CODE);
        }else {
            Intent intent = new Intent(this, LoginView.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 권한 승인 여부 체크
        if(requestCode == REQ_CODE){
            boolean granted = true;
            for(int grant : grantResults){
                if(grant != PackageManager.PERMISSION_GRANTED){
                    granted = false;
                    break;
                }
            }
            if(granted){
                Intent intent = new Intent(this, LoginView.class);
                startActivity(intent);
                finish();
            }
        }
    }
}