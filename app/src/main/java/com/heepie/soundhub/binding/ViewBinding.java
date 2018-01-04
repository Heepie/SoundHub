package com.heepie.soundhub.binding;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.heepie.soundhub.BuildConfig;
import com.heepie.soundhub.R;
import com.heepie.soundhub.controller.PlayerController;
import com.heepie.soundhub.domain.model.Post;
import com.heepie.soundhub.domain.model.User_Post;
import com.heepie.soundhub.utils.Const;
import com.heepie.soundhub.utils.MusicUtil;
import com.heepie.soundhub.viewmodel.DetailViewModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import kotlin.io.ByteStreamsKt;
import rm.com.audiowave.AudioWaveView;
import rm.com.audiowave.OnProgressListener;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * Created by Heepie on 2017. 11. 25..
 */

public class ViewBinding {
    @BindingAdapter("loadImage")
    public static void setLoadImage(ImageView view, String path) {
        String url = BuildConfig.FILE_SERVER_URL + "media/" + path;
        RequestOptions options = new RequestOptions()
                                            .centerCrop()
                                            .placeholder(R.drawable.piano)
                                            .error(R.drawable.piano);

        Glide.with(view.getContext())
                .load(path)
                .load(url)
                .apply(options)
                .into(view);
    }


    @BindingAdapter("loadImage")
    public static void setLoadImage(ImageView view, Uri uri) {
        Glide.with(view.getContext())
                .load(uri)
                .into(view);
    }

    @BindingAdapter("loadImage")
    public static void setLoadImage(ImageView view, int resId) {
        Glide.with(view.getContext())
                .load(resId)
                .into(view);
    }

    @BindingAdapter("loadCircleImage")
    public static void setLoadCircleImage(ImageView view, int resId) {
        Glide.with(view.getContext())
                .load(resId)
                .apply(bitmapTransform(new CircleCrop()))
                .into(view);
    }

    @BindingAdapter("loadBlurImage")
    public static void setLoadBlurImage(ImageView view, int resId) {
        // TODO Glide 4.+ Blur 처리 알아보기
        Glide.with(view.getContext())
                .load(resId)
                .into(view);
    }

    // Audio 파일을 Wave로 바꿔주는 Adapter
    @BindingAdapter("setWave")
    public static void setWave(AudioWaveView audioWaveView, String path) {
        // 필요한 변수 선언
        PlayerController player = PlayerController.getInstance();
        FileInputStream inputStream = null;
        int time = 0;
        byte[] musicByte=null;

        // Music Duration 추출
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            inputStream = new FileInputStream(path);
            retriever.setDataSource(inputStream.getFD());
            time = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));

            Log.d("ViewBinding", "setWave: " + time);
        } catch (Exception e) {

        }

        try {
            musicByte = ByteStreamsKt.readBytes(new FileInputStream(new File(path)), 0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        final int duration = time;

        if (musicByte != null && path != null) {
            audioWaveView.setRawData(musicByte, () -> {
                player.setMasterMusic(path, duration);
            });
        }

        audioWaveView.setOnProgressListener(new OnProgressListener() {
            @Override
            public void onStartTracking(float v) {
                Log.d("setWave", "onStartTracking: " + v);
                player.setCurPlayer(MusicUtil.percentToDuration(v, duration));
                audioWaveView.setProgress(v);
            }

            @Override
            public void onStopTracking(float v) {

            }

            @Override
            public void onProgressChanged(float v, boolean b) {

            }
        });

    }

    @BindingAdapter("setCurProgr")
    public static void setCurProgr(View view, float curProgress) {
        if (view instanceof AudioWaveView) {
            if (100 >= curProgress)
                ((AudioWaveView) view).setProgress(curProgress);
            // progress bar 터치 이벤트 설정
        }
    }

    @BindingAdapter("setVisible")
    public static void setVisible(View view, String data) {
        if (data == null)
            view.setVisibility(View.INVISIBLE);
        else
            view.setVisibility(View.VISIBLE);
    }

    @BindingAdapter("setCountVisible")
    public static void setCountVisible(View view, String data) {
        if ("-1".equals(data))
            view.setVisibility(View.INVISIBLE);
        else
            view.setVisibility(View.VISIBLE);
    }

    @BindingAdapter("setMergeVisible")
    public static void setMergeVisible(View view, Post model) {
        if (Const.user.getNickname().equals(model.getAuthor().getNickname()))
            view.setVisibility(View.VISIBLE);
        else
            view.setVisibility(View.GONE);
    }

    @BindingAdapter("setLikeImage")
    public static void setLikeImage(View view, Post model) {
        if (Const.user.getLiked_posts() != null) {
            for (User_Post item : Const.user.getLiked_posts()) {
                if (item.getId().equals(model.getId())) {
                    view.setBackgroundDrawable(
                            view.getContext().getResources().
                                    getDrawable(R.drawable.icon_like));
                    ((ToggleButton) view).setChecked(true);
                    return;
                }
            }
        }
    }
}
