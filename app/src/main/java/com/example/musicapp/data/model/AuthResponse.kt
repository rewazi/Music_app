package com.example.musicapp.data.model

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("user_id") val userId: Int? = null
)
