package com.example.wk01h02;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi {

    @GET("posts")
    Call<List<Post>> getPosts();

    @GET("posts")
    Call<List<Post>> getPostsByUserID(
            @Query("userId") String userId
    );

    @GET("users")
    Call<List<User>> getUsers();

    @GET("users")
    Call<List<User>> getUserByID(
            @Query("id") String userId
    );

    @GET("users")
    Call<List<User>> getUserByCredentials(
            @Query("username") String username,
            @Query("email") String email
    );
}
