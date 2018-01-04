package com.heepie.soundhub.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

/**
 * Created by eunmin on 2017-12-15.
 */

public class User_Post implements Parcelable {


    private String id;
    private String title;
    private String genre;
    private String instrument;
    private String num_liked;
    private String num_comments;
    private String created_date;
    private User author;
    private String post_img;

    public User_Post() {

    }

    public User_Post(String id, String title, String genre, String instrument, String num_liked, String num_comments, String created_date, User author) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.instrument = instrument;
        this.num_liked = num_liked;
        this.num_comments = num_comments;
        this.created_date = created_date;
        this.author = author;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public String getNum_liked() {
        return num_liked;
    }

    public void setNum_liked(String num_liked) {
        this.num_liked = num_liked;
    }

    public String getNum_comments() {
        return num_comments;
    }

    public void setNum_comments(String num_comments) {
        this.num_comments = num_comments;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getPost_img() {
        return post_img;
    }

    public void setPost_img(String post_img) {
        this.post_img = post_img;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.genre);
        dest.writeString(this.instrument);
        dest.writeString(this.num_liked);
        dest.writeString(this.num_comments);
        dest.writeString(this.created_date);
        dest.writeParcelable(this.author, flags);
    }

    protected User_Post(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.genre = in.readString();
        this.instrument = in.readString();
        this.num_liked = in.readString();
        this.num_comments = in.readString();
        this.created_date = in.readString();
        this.author = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<User_Post> CREATOR = new Creator<User_Post>() {
        @Override
        public User_Post createFromParcel(Parcel source) {
            return new User_Post(source);
        }

        @Override
        public User_Post[] newArray(int size) {
            return new User_Post[size];
        }
    };
}
