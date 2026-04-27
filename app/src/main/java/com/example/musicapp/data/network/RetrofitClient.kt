package com.example.musicapp.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Replace with your local IP address for XAMPP (usually 10.0.2.2 for Android Emulator)
    private const val BASE_URL = "http://10.0.2.2/musicapp/"

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
