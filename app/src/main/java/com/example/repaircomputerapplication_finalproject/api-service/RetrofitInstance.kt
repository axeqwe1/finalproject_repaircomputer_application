package com.example.repaircomputerapplication_finalproject.`api-service`
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.repaircomputerapplication_finalproject.viewModel.ContextDataStore.dataStore
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.net.CookieManager
import java.net.CookiePolicy


class RetrofitInstance(application: Application) : AndroidViewModel(application) {
    val context = application.dataStore
    val cookieManager = CookieManager().apply {
        setCookiePolicy(CookiePolicy.ACCEPT_ALL)
    }
    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    // สร้าง Interceptor
//    val authInterceptor = Interceptor { chain ->
//        val originalRequest = chain.request()
//        val requestBuilder = originalRequest.newBuilder()
//
//        // ดึง Session ID จาก DataStore (ใช้ Coroutine)
//        val dataStore = context // หรือวิธีที่คุณสร้าง DataStore
//        runBlocking {
//            dataStore.data.firstOrNull()?.get(stringPreferencesKey("session_key"))
//                ?.let { sessionId ->
//                    requestBuilder.addHeader(
//                        "Cookie",
//                        "session_id=$sessionId"
//                    ) // ปรับแต่ง header ตามความเหมาะสม
//                }
//        }
//
//        chain.proceed(requestBuilder.build())
//    }

    val cookieJar = JavaNetCookieJar(cookieManager)
    val client = OkHttpClient.Builder()
        .cookieJar(cookieJar)
//        .addInterceptor(authInterceptor)
//        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.100:8000/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(IAPIService::class.java)
}

