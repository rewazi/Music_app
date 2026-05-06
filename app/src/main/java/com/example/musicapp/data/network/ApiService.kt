package com.example.musicapp.data.network

import com.example.musicapp.data.model.Album
import com.example.musicapp.data.model.AuthResponse
import com.example.musicapp.data.model.Song
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
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

    @GET("get_albums.php")
    suspend fun getAlbums(): List<Album>

    @GET("get_songs.php")
    suspend fun getSongs(
        @retrofit2.http.Query("album_id") albumId: Int
    ): List<Song>
    @FormUrlEncoded
    @POST("update_profile.php")
    suspend fun updateProfile(
        @Field("user_id") userId: Int,
        @Field("username") username: String
    ): AuthResponse
}
