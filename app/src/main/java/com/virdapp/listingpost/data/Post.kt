package com.virdapp.listingpost.data

import androidx.annotation.DrawableRes
import com.virdapp.listingpost.R

data class Post(

    val title: String,
    val description: String,
    @DrawableRes val image: Int,
    val views: Int,
    val author:String,
   @DrawableRes val logo_of_author:Int,
    val likes_no: Int,
    val comments:Int,
    val blogPost: BlogPost,
    val rendered: Rendered
)


//
//val news_post = listOf(
//    Post(
//        "First Post",
//        "This first post is generated to test the dummy application .",
//        R.drawable.image1,
//        200,
//        "Woof App",
//        R.drawable.ic_woof_logo,
//        99,
//        10,
//
//    ),
//    Post(
//            "Second Post",
//    "This second post is generated to test the dummy application which the listing page app assignment given by vird company.",
//    R.drawable.image1,
//    1000,
//    "Woof App",
//    R.drawable.ic_woof_logo,
//    990,
//    900
//)
//)

