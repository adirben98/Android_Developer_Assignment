package com.example.android_developer_assignment.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.android_developer_assignment.Model.Model;
import com.example.android_developer_assignment.Model.User;

import java.util.List;

public class UsersListViewModel extends ViewModel {
    private LiveData<List<User>> users;

    public LiveData<List<User>> getUsers() {
        if (users == null) {
            users = Model.instance().getUsers();
        }
        return users;
    }

}
