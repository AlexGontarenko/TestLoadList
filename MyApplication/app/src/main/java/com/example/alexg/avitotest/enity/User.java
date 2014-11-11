package com.example.alexg.avitotest.enity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by AlexG on 11.11.2014.
 */
public class User implements Parcelable {

    private String avatarUrl;
    private String login;

    public User() { }

    public User(String avatarUrl, String login) {
        super();
        this.avatarUrl = avatarUrl;
        this.login = login;
    }

    protected User(Parcel in) {
        avatarUrl = in.readString();
        login = in.readString();
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(avatarUrl);
        dest.writeString(login);
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
