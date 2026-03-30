package com.example.musicapp

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("register.php")
    suspend fun register(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): AuthResponse

    @FormUrlEncoded
    @POST("login.php")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): AuthResponse
}
