package com.virdapp.listingpost.data

import android.util.Log
import androidx.annotation.DrawableRes
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import com.virdapp.listingpost.R
import com.virdapp.listingpost.data.author_data.AuthorDetails
import com.virdapp.listingpost.remote.BlogApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext



data class Post(

    val title: String,
    val description: String,
    @DrawableRes val image: Int,
    val views: Int,
    var author:String = "",
     var logo_of_author: String = "",
    val likes_no: Int,
    val comments:Int,
    val blogPost: BlogPost,
    val rendered: Rendered,
    var author_id: Int = 0,
    var authorDetails: List<AuthorDetails>,
    var shareLink: String,
    val featuredMediaUrl: String,
    val link: String,
)

