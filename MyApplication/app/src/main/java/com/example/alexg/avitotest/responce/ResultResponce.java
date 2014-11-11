package com.example.alexg.avitotest.responce;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.alexg.avitotest.enity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AlexG on 11.11.2014.
 */
public class ResultResponce implements Parcelable {

    private boolean status;
    private List<User> users;

    public ResultResponce() {
        super();
    }

    public ResultResponce(boolean status, List<User> users) {
        super();
        this.status = status;
        this.users = users;
    }

    protected ResultResponce(Parcel in) {
        status = in.readByte()==1;
        if(in.readInt() == 1)
            users = in.createTypedArrayList(User.CREATOR);
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isOk() {
        return status;
    }

    public int getCount() {
        return users == null ? 0 : users.size();
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void add(User user) {
        if (users == null)
            users = new ArrayList<User>();
        users.add(user);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (status?1:0));
        dest.writeInt(users != null ? 1 : 0);
        if (users != null)
            dest.writeTypedList(users);
    }

    public static final Parcelable.Creator<ResultResponce> CREATOR = new Parcelable.Creator<ResultResponce>() {
        public ResultResponce createFromParcel(Parcel in) {
            return new ResultResponce(in);
        }

        public ResultResponce[] newArray(int size) {
            return new ResultResponce[size];
        }
    };
}
