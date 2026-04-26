package com.example.gameapp.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Singleton object for Retrofit instance
 * Handles API client configuration and provides CheapShark API service
 */
object RetrofitInstance {
    private const val BASE_URL = "https://www.cheapshark.com/api/1.0/"
    private const val TIMEOUT_SECONDS = 30L

    /**
     * Create OkHttp client with logging interceptor
     */
    private fun createOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }

    /**
     * Lazy initialization of Retrofit instance
     * This ensures the client is created only once
     */
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(createOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * Lazy initialization of API service
     * Provides the CheapShark API service for making requests
     */
    val apiService: CheapSharkApiService by lazy {
        retrofit.create(CheapSharkApiService::class.java)
    }
}
