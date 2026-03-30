package com.example.musicapp

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("user_id") val userId: Int? = null
)

data class Album(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("singer_name") val singerName: String,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("banner_url") val bannerUrl: String
)

data class Song(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("singer_name") val singerName: String,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("banner_url") val bannerUrl: String,
    @SerializedName("song_url") val songUrl: String
)
