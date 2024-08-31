package com.example.android_developer_assignment.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android_developer_assignment.Model.Model;
import com.example.android_developer_assignment.Model.User;

public class EditUserViewModel extends ViewModel {

    private LiveData<User> user;

    LiveData<User> getUserById(int id) {
        user =  Model.instance().getUserById(id);
        return user;
    }

}
