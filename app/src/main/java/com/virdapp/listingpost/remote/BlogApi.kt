package com.virdapp.listingpost.remote

import RespCacheObject
import ResponseCacheInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object BlogApi  {
    private const val BASE_URL = "https://blog.vrid.in/"


    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val ResponseCacheInterceptor = ResponseCacheInterceptor(RespCacheObject.getCacheDao())
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(ResponseCacheInterceptor)
        .build()
    val instance: BlogApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BlogApiService::class.java)
    }

}