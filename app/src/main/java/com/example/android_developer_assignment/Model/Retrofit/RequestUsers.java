package com.example.android_developer_assignment.Model.Retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RequestUsers {
    @GET("/api/users")
    Call<ReqResResponse> getUsers(@Query("page") int id);
}

