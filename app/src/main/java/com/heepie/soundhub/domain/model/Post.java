package com.heepie.soundhub.domain.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.heepie.soundhub.BR;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Heepie on 2017. 11. 30..
 */

public class Post extends BaseObservable implements Parcelable {
    private String author_track;

    private String created_date;

    private String genre;

    private String id;

    private String num_comments;

    private String num_liked;

    private String author;

    private String title;

    private String master_track;

    private String[] liked;

    @Bindable
    private Map<String, List<Comment_track>> comment_tracks;

    private String instrument;

    public Post() {

    }


    public String getAuthor_track() {
        return author_track;
    }

    public void setAuthor_track(String author_track) {
        this.author_track = author_track;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Bindable
    public String getNum_comments() {
        return num_comments;
    }

    public void setNum_comments(String num_comments) {
        this.num_comments = num_comments;
        notifyPropertyChanged(BR.num_comments);
    }

    @Bindable
    public String getNum_liked() {
        return num_liked;
    }

    public void setNum_liked(String num_liked) {
        this.num_liked = num_liked;
        notifyPropertyChanged(BR.num_liked);
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMaster_track() {
        return master_track;
    }

    public void setMaster_track(String master_track) {
        this.master_track = master_track;
    }

    public String[] getLiked() {
        return liked;
    }

    public void setLiked(String[] liked) {
        this.liked = liked;
    }

    @Bindable
    public Map<String, List<Comment_track>> getComment_tracks() {
        return comment_tracks;
    }

    public void setComment_tracks(Map<String, List<Comment_track>> comment_tracks) {
        this.comment_tracks = comment_tracks;
        notifyPropertyChanged(BR.comment_tracks);
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    @Override
    public String toString() {
        return "Post{" +
                "author_track='" + author_track + '\'' +
                ", created_date='" + created_date + '\'' +
                ", genre='" + genre + '\'' +
                ", id='" + id + '\'' +
                ", num_comments='" + num_comments + '\'' +
                ", num_liked='" + num_liked + '\'' +
                ", author=" + author +
                ", title='" + title + '\'' +
                ", master_track='" + master_track + '\'' +
                ", liked=" + Arrays.toString(liked) +
                ", comment_tracks=" + comment_tracks +
                ", instrument='" + instrument + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.author_track);
        dest.writeString(this.created_date);
        dest.writeString(this.genre);
        dest.writeString(this.id);
        dest.writeString(this.num_comments);
        dest.writeString(this.num_liked);
        dest.writeString(this.author);
        dest.writeString(this.title);
        dest.writeString(this.master_track);
        dest.writeStringArray(this.liked);
        if (this.comment_tracks != null) {
            dest.writeInt(this.comment_tracks.size());
            for (Map.Entry<String, List<Comment_track>> entry : this.comment_tracks.entrySet()) {
                dest.writeString(entry.getKey());
                dest.writeList(entry.getValue());
            }
        }
        dest.writeString(this.instrument);
    }

    protected Post(Parcel in) {
        this.author_track = in.readString();
        this.created_date = in.readString();
        this.genre = in.readString();
        this.id = in.readString();
        this.num_comments = in.readString();
        this.num_liked = in.readString();
        this.author = in.readString();
        this.title = in.readString();
        this.master_track = in.readString();
        this.liked = in.createStringArray();
        int comment_tracksSize = in.readInt();
        if (this.comment_tracks != null) {
            this.comment_tracks = new HashMap<String, List<Comment_track>>(comment_tracksSize);
            for (int i = 0; i < comment_tracksSize; i++) {
                String key = in.readString();
                List<Comment_track> value = new ArrayList<Comment_track>();
                in.readList(value, Comment_track.class.getClassLoader());
                this.comment_tracks.put(key, value);
            }
        }
        this.instrument = in.readString();
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel source) {
            return new Post(source);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };
}

