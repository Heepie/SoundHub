package com.heepie.soundhub.controller;

import android.content.Context;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Heepie on 2017. 12. 6..
 */

public class RecordController {
    private final String TAG = getClass().getSimpleName();
    private static RecordController instance;

    private MediaRecorder mRecorder;
    private String mFileName;

    private Context context;

    private RecordController() {
        mRecorder = new MediaRecorder();
        /*mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        *//*mRecorder.setProfile();
        CamcorderProfile profile = new CamcorderProfile();*/
    }

    public static RecordController getInstance() {
        if (instance == null) {
            instance = new RecordController();
        }
        return instance;
    }


    public void startRecording() {
        try {
            prepareRecord();
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    public String stopRecording() {
        mRecorder.stop();
//        mRecorder.release();
//        mRecorder = null;

        return mFileName;
    }

    public void initRecorder(Context context) {
        this.context = context;

        /*mFileName = context.getExternalCacheDir().getAbsolutePath();
        // 임시 파일 이름
        mFileName += "/record.mp3";
        mRecorder.setOutputFile(mFileName);*/
    }

    public void prepareRecord() {
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        mFileName = context.getExternalCacheDir().getAbsolutePath();
        // 임시 파일 이름
        mFileName += "/record.mp3";
        mRecorder.setOutputFile(mFileName);
    }
}
