package com.heepie.soundhub.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.heepie.soundhub.BR;
import com.heepie.soundhub.BuildConfig;
import com.heepie.soundhub.R;
import com.heepie.soundhub.adapter.ExpandListAdapter;
import com.heepie.soundhub.controller.PlayerController;
import com.heepie.soundhub.Interfaces.ICallback;
import com.heepie.soundhub.controller.RecordController;
import com.heepie.soundhub.domain.logic.CommentAPI;
import com.heepie.soundhub.domain.logic.FileApi;
import com.heepie.soundhub.domain.logic.PostApi;
import com.heepie.soundhub.domain.model.Comment_track;
import com.heepie.soundhub.domain.model.Post;
import com.heepie.soundhub.utils.Const;
import com.heepie.soundhub.view.RecordView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Created by Heepie on 2017. 12. 1..
 */

public class DetailViewModel {
    public final String TAG = getClass().getSimpleName();
    public static DetailViewModel instance;

    public PlayerController player;
    private RecordController recorder;
    private CommentAPI commentAPI;
    private PostApi postApi;

    private Post post;
    public ArrayList<String> urls;
    private String url;
    private Context context;
    private String mRecordFilePath;

    private StringBuilder urlBuilder;
    public ObservableField<String> masterPath;
    public ObservableField<String> selectedInstrument;
    public ObservableField<String> countDown;

    public List<String> selectedTrack;
    private String waveTrack;

    private ExpandListAdapter adapter;
    private ExpandableListView exListView;

    public static DetailViewModel getInstance() {
        if (instance == null)
            instance = new DetailViewModel();
        return instance;
    }

    private DetailViewModel() {
        urls = new ArrayList<>();
        masterPath = new ObservableField<>(" ");
        selectedInstrument = new ObservableField<>(" ");
        countDown = new ObservableField<>(" ");
        commentAPI = CommentAPI.getInstance();
        selectedTrack = new ArrayList<>();

        postApi = PostApi.getInstance();
    }

    public void initContext(Activity activity) {
        this.context = activity;
        player = PlayerController.getInstance();
        player.initPlayer(context);

        recorder = RecordController.getInstance();
        recorder.initRecorder(context);
    }

    public void setPost(Post post, ExpandableListView exListView, ExpandListAdapter adapter) {
        this.post = post;

        // 무조건 실행되는 Author 트랙 초기 설정
        waveTrack = post.getMaster_track()  != null ? post.getMaster_track() : post.getAuthor_track();

        Log.d(TAG, "setPost: " + waveTrack);

        urlBuilder = new StringBuilder();
        urlBuilder.append(BuildConfig.FILE_SERVER_URL)
                  .append("media/")
                  .append(waveTrack);

        url = urlBuilder.toString();

        urls.clear();
        urls.add(urlBuilder.toString());
        setMasterTrackWave(url);

        this.adapter = adapter;
        this.exListView = exListView;
        ArrayList<String> groups = new ArrayList<>(post.getComment_tracks().keySet());
        adapter.setDataAndRefresh(groups, post.getComment_tracks());
        Log.d(TAG, "setPost: " + adapter.getGroupCount());

        for (int i=0; i<groups.size(); i=i+1)
            exListView.expandGroup(i);
    }

    String masterPahString;

    private void setMasterTrackWave(String url) {
        FileApi.getInstance().getMusic(context, url, post.getId(), new ICallback() {
            @Override
            public void initData(int code, String msg, Object data) {
                Log.i("heepie", code+" " + msg + " " + data);
                masterPahString = (String)data;
                masterPath.set(masterPahString);
            }
        });
    }

    public void onClickedMerge(View view, Activity activity, ProgressBar progressBar) {
        checkSelectedTrack();
        progressBar.setVisibility(View.VISIBLE);
        postApi.requestMerge(post.getId(), selectedTrack, (code, msg, data) -> {
            Observable<Response<Post>> postObs = postApi.getPost(post.getId());
                postObs.subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.newThread())
                        .subscribe(
                                jsonData -> {
                                    try {
                                        Thread.sleep(8000);
                                    } catch (Exception e) {

                                    }
                                    if (jsonData.isSuccessful()) {
                                        waveTrack = jsonData.body().getMaster_track();
                                    }
                                },
                                throwable -> {
                                },
                                () -> {
                                    Log.d(TAG, "onClickedMerge: " + post.getMaster_track());
                                    urlBuilder = new StringBuilder();
                                    urlBuilder.append(BuildConfig.FILE_SERVER_URL)
                                            .append("media/")
                                            .append(waveTrack);

                                    url = urlBuilder.toString();
                                    setMasterTrackWave(url);

                                    activity.runOnUiThread(() -> progressBar.setVisibility(View.GONE));
                                });
        });
    }

    public void onClickedPlayPause(View view, ProgressBar progressBar) {
        Log.d(TAG, "onClickedPlayPause: Clicked " + PlayerController.selectMusicStatus);

        switch (PlayerController.selectMusicStatus) {
            case Const.ACTION_SELECT_MUSIC_NOT_INIT:
                checkSelectedTrack();
                Log.d(TAG, "onClickedPlayPause: " + urls.toString());
                progressBar.setVisibility(View.VISIBLE);
                player.setMusic(urls, (code, msg, data) -> {
                    if (code == Const.RESULT_SUCCESS) {
                        player.play();
                    }
                });
                ((Button)view).setText("일시정지");
                progressBar.setVisibility(View.GONE);

                break;
            case Const.ACTION_SELECT_MUSIC_PAUSE:
                if(player.play())
                    ((Button)view).setText("일시정지");
                break;

            case Const.ACTION_SELECT_MUSIC_PLAY:
                player.pause();
                ((Button)view).setText("재생");
                break;
        }
    }

    private boolean onRecording = false;

    public void onClickedUpLoad(View view) {
        Log.d(TAG, "onClickedUpLoad: Clicked");
        checkSelectedTrack();

        Intent intent = new Intent(view.getContext(), RecordView.class);
        view.getContext().startActivity(intent);
    }

    // 체크박스로 선택된 track 추출
    private void checkSelectedTrack() {
        selectedTrack.clear();
        for(String instrument : post.getComment_tracks().keySet()) {
            for(Comment_track track : post.getComment_tracks().get(instrument)) {
                Log.d(TAG, "checkSelectedTrack: " + instrument + ":\t" + track.getId() + " " + track.getIsCheck());
                if (track.getIsCheck()) {
                    selectedTrack.add(track.getId());
                    Log.d(TAG, "checkSelectedTrack: " + track.getId());
                    urlBuilder = new StringBuilder();
                    urlBuilder.append(BuildConfig.FILE_SERVER_URL)
                            .append("media/")
                            .append(track.getComment_track());

                    Log.d(TAG, "checkSelectedTrack: " + urlBuilder.toString());

                    urls.add(urlBuilder.toString());
                }
            }
        }

        /*for (String url : urls)
            Log.d(TAG, "onClickedMerge: " + url);*/
    }

    public void onPause() {
        player.pause();
        player.stopPlaying();
    }

    public void onDestory() {
        onPause();
        player.initData();
        masterPath.set(" ");
    }


    public void onClickedRecord(View v, View targetView) {
        // 녹음 기능
        onRecording = (onRecording == true) ? false : true;

        if (onRecording) {
            // 녹음 진행 시작
            player.setMusic(urls, null);
            Log.d(TAG, "onClickedRecord: " + urls.toString());

            checkSelectedTrack();
            targetView.setVisibility(View.VISIBLE);

            Observable<Integer> createCounter = Observable.create(new ObservableOnSubscribe<Integer>() {
                @Override
                public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                    try {
                        for (int i=3; i>=-1; i=i-1) {
                            e.onNext(i);
                        }
                        e.onComplete();
                    } catch (Exception ex) {

                    }
                }
            });

            createCounter.observeOn(Schedulers.io())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(
                            count -> {
                                Log.d(TAG, "onClickedRecord: " + count);
                                if (count == 0) {
                                    countDown.set("START!");

                                } else {
                                    countDown.set(count+"");
                                }
                                if (count != -1)
                                    Thread.sleep(1000);

                            },
                            // Error 처리
                            throwable -> {},
                            // Complete 처리
                            () ->{
                                player.play();
                                recorder.startRecording();
                            }
                    );
            ((ImageButton)v).setImageResource(android.R.drawable.ic_media_pause);
        } else {
            // 녹음이 진행 중지
            mRecordFilePath = recorder.stopRecording();
            // 녹음을 멈추고 재생 시작
            player.setMasterMusic(mRecordFilePath, 0);
            player.pause();
            ((ImageButton)v).setImageResource(R.drawable.icon_record);
            Toast.makeText(context, "녹음 완료!", Toast.LENGTH_SHORT).show();
        }
    }

    public void onUploadFrAudio (View v, ProgressBar progress_bar, Activity callFrom) {
        if (mRecordFilePath == null) {
            Toast.makeText(v.getContext(), "먼저 녹음을 해주세요.", Toast.LENGTH_SHORT).show();
        } else if(" ".equals(selectedInstrument.get()) || "Select your instrument".equals(selectedInstrument.get())) {
            Toast.makeText(v.getContext(), "Select your instrument", Toast.LENGTH_SHORT).show();
        } else {

            commentAPI.pushComment(post.getId(), selectedInstrument.get(), mRecordFilePath,
            (code, msg, body) -> {
                progress_bar.setVisibility(View.VISIBLE);
                switch (code) {
                    case Const.RESULT_SUCCESS:
                        Log.d(TAG, "onUploadFrAudio: " + code);
                        Observable<Response<Post>> postObs = postApi.getPost(post.getId());

                        postObs.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        jsonData -> {
                                            if (jsonData.isSuccessful()) {
                                                post = null;
                                                post = jsonData.body();
                                                Log.d(TAG, "onUploadFrAudio: Before " + post.toString());
                                            }
                                        },
                                        throwable -> {},
                                        // Complete 처리
                                        () -> {
                                            if (post != null) {
                                                Log.d(TAG, "onUploadFrAudio: " + post.toString());
                                                setPost(post, exListView, adapter);
                                                progress_bar.setVisibility(View.GONE);
                                                callFrom.finish();
                                            }
                                        }
                                );
                        break;
                }
            });
        }
    }

    public void onUploadFrFile (View v, View filePath, ProgressBar progress_bar, Activity callFrom) {
        if (filePath == null) {
            Toast.makeText(v.getContext(), "먼저 파일을 선택해 해주세요.", Toast.LENGTH_SHORT).show();
        } else if (" ".equals(selectedInstrument.get()) || "Select your instrument".equals(selectedInstrument.get())) {
            Toast.makeText(v.getContext(), "Select your instrument", Toast.LENGTH_SHORT).show();
        } else {
            progress_bar.setVisibility(View.VISIBLE);
            commentAPI.pushComment(post.getId(), selectedInstrument.get(), ((TextView)filePath).getText().toString(),
                    (code, msg, body) -> {
                        if (code == Const.RESULT_SUCCESS) {
                            Observable<Response<Post>> postObs = postApi.getPost(post.getId());

                            postObs.subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                            jsonData -> {
                                                if (jsonData.isSuccessful()) {
                                                    post = null;
                                                    post = jsonData.body();
                                                    Log.d(TAG, "onUploadFrFile: Before " + post.toString());
                                                }
                                            },
                                            throwable -> {},
                                            // Complete 처리
                                            () -> {
                                                if (post != null) {
                                                    Log.d(TAG, "onUploadFrFile: " + post.toString());
                                                    setPost(post, exListView, adapter);
                                                    progress_bar.setVisibility(View.GONE);
                                                    callFrom.finish();
                                                }
                                            }
                                    );
                        }
                    });
        }
    }

    public void onClickedRepeat(View v) {
        if (mRecordFilePath != null) {
            player.stopPlaying();
            player.setMasterMusic(mRecordFilePath, 0);
            player.startPlaying();
        } else {
            Toast.makeText(v.getContext(), "먼저 녹음을 해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickedMasterPlay(View view) {
        switch (PlayerController.masterStatus) {
            case Const.ACTION_MASTER_PLAY:
                 Glide.with(context)
                      .load(R.drawable.music_play)
                      .into((ImageButton)view);
                player.pausePlayer();
                break;

            case Const.ACTION_MASTER_PREPARE:
                 Glide.with(context)
                      .load(R.drawable.music_pause)
                      .into((ImageButton)view);
                player.startPlaying();
                break;
            case Const.ACTION_MASTER_PAUSE:
                 Glide.with(context)
                      .load(R.drawable.music_pause)
                      .into((ImageButton)view);
                player.startPlaying();
                break;
        }

    }
}