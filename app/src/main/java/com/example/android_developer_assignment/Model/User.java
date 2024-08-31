package com.example.android_developer_assignment.Model;

import android.content.Context;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.android_developer_assignment.MyApplication;

@Entity
public class User {
    @PrimaryKey
    int id;
    String email, first_name, last_name, avatar;

    public User(int id, String email, String first_name, String last_name,String avatar) {
        this.id = id;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.avatar = avatar;

    }



    static void setIsUpdated(boolean isUpdate) {
        MyApplication.getMyContext().getSharedPreferences("User", Context.MODE_PRIVATE).edit().putBoolean("IS_UPDATED", isUpdate).apply();
    }

    static public boolean IsUpdated() {
        return MyApplication.getMyContext().getSharedPreferences("User", Context.MODE_PRIVATE).getBoolean("IS_UPDATED", false);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
