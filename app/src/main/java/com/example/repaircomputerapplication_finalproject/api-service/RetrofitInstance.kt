package com.example.repaircomputerapplication_finalproject.api_service

import com.example.repaircomputerapplication_finalproject.`api-service`.IAPIService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val client = OkHttpClient.Builder()
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.33:8000/") // URL ของ Server ของคุณ
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: IAPIService = retrofit.create(IAPIService::class.java)
}
