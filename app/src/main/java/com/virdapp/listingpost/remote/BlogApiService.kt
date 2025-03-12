package com.virdapp.listingpost.remote

import com.virdapp.listingpost.data.BlogPost
import com.virdapp.listingpost.data.Post
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface BlogApiService {


    @GET("wp-json/wp/v2/posts")
    suspend fun getPosts(
        @Query("per_page") perPage: Int = 10,
        @Query("page") page: Int = 1
    ): List<BlogPost>

}