package com.example.android_developer_assignment.Model.Retrofit;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RequestUsers {
    @GET("/api/users?page=2")
    Call<ReqResResponse> getUsers();
}
