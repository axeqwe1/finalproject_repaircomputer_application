package com.example.repaircomputerapplication_finalproject.api_service

import com.example.repaircomputerapplication_finalproject.`api-service`.IAPIService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {

    private val logging = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://45.136.255.62:8000/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: IAPIService = retrofit.create(IAPIService::class.java)
}
