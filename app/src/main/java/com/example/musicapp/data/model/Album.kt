package com.example.musicapp.data.model

import com.google.gson.annotations.SerializedName

data class Album(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("singer_name") val singerName: String,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("banner_url") val bannerUrl: String
)
