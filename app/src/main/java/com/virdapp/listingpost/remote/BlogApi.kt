package com.virdapp.listingpost.remote

import ResponseCacheInterceptor
import android.content.Context
import com.virdapp.listingpost.cache.PostDatabase
import com.virdapp.listingpost.cache.UrlCache
import com.virdapp.listingpost.cache.UrlCacheDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.Response
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