package com.heepie.soundhub.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Heepie on 2017. 11. 30..
 */

public class User implements Parcelable
{
    private String total_liked;
    private String id;
    private String is_active;
    private String is_staff;
    private String nickname;
    private String email;
    private String instrument;
    private String last_login;
    private String genre; //
    private List<User_Post> post_set = new ArrayList<>();//
    private List<User_Post> liked_posts = new ArrayList<>();//
    private List<String> followers = new ArrayList<>();
    private List<String> following = new ArrayList<>();
    private String num_followings;//
    private String num_followers;
    private String user_type;
    private String profile_img;
    private String profile_bg;

    public User(){

    }

    public User(String total_liked, String id, String is_active, String is_staff, String nickname, String email, String instrument, String last_login, String genre, List<User_Post> post_set, List<User_Post> liked_posts, List<String> followers, List<String> following, String num_followings, String num_followers, String user_type) {
        this.total_liked = total_liked;
        this.id = id;
        this.is_active = is_active;
        this.is_staff = is_staff;
        this.nickname = nickname;
        this.email = email;
        this.instrument = instrument;
        this.last_login = last_login;
        this.genre = genre;
        this.post_set = post_set;
        this.liked_posts = liked_posts;
        this.followers = followers;
        this.following = following;
        this.num_followings = num_followings;
        this.num_followers = num_followers;
        this.user_type = user_type;
    }

    public String getTotal_liked() {
        return total_liked;
    }

    public void setTotal_liked(String total_liked) {
        this.total_liked = total_liked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getIs_staff() {
        return is_staff;
    }

    public void setIs_staff(String is_staff) {
        this.is_staff = is_staff;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public String getLast_login() {
        return last_login;
    }

    public void setLast_login(String last_login) {
        this.last_login = last_login;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public List<User_Post> getLiked_posts() {
        return liked_posts;
    }

    public void setLiked_posts(List<User_Post> liked_posts) {
        this.liked_posts = liked_posts;
    }

    public String getNum_followings() {
        return num_followings;
    }

    public void setNum_followings(String num_followings) {
        this.num_followings = num_followings;
    }

    public List<String> getFollowing() {
        return following;
    }

    public void setFollowing(List<String> following) {
        this.following = following;
    }

    public String getNum_followers() {
        return num_followers;
    }

    public void setNum_followers(String num_followers) {
        this.num_followers = num_followers;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public List<String> getFollowers() {
        return followers;
    }

    public void setFollowers(List<String> followers) {
        this.followers = followers;
    }

    public List<User_Post> getPost_set() {
        return post_set;
    }

    public void setPost_set(List<User_Post> post_set) {
        this.post_set = post_set;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }

    public String getProfile_bg() {
        return profile_bg;
    }

    public void setProfile_bg(String profile_bg) {
        this.profile_bg = profile_bg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.total_liked);
        dest.writeString(this.id);
        dest.writeString(this.is_active);
        dest.writeString(this.is_staff);
        dest.writeString(this.nickname);
        dest.writeString(this.email);
        dest.writeString(this.instrument);
        dest.writeString(this.last_login);
        dest.writeString(this.genre);
        dest.writeTypedList(this.post_set);
        dest.writeTypedList(this.liked_posts);
        dest.writeStringList(this.followers);
        dest.writeStringList(this.following);
        dest.writeString(this.num_followings);
        dest.writeString(this.num_followers);
        dest.writeString(this.user_type);
        dest.writeString(this.profile_img);
        dest.writeString(this.profile_bg);

    }

    protected User(Parcel in) {
        this.total_liked = in.readString();
        this.id = in.readString();
        this.is_active = in.readString();
        this.is_staff = in.readString();
        this.nickname = in.readString();
        this.email = in.readString();
        this.instrument = in.readString();
        this.last_login = in.readString();
        this.genre = in.readString();
        in.readTypedList(this.post_set, User_Post.CREATOR);
        in.readTypedList(this.liked_posts, User_Post.CREATOR);
        in.readStringList(this.followers);
        in.readStringList(this.following);
        this.num_followings = in.readString();
        this.num_followers = in.readString();
        this.user_type = in.readString();
        this.profile_img = in.readString();
        this.profile_bg = in.readString();

    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}