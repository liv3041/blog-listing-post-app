package com.virdapp.listingpost.data

import android.icu.text.CaseMap
import com.google.gson.annotations.SerializedName
import com.virdapp.listingpost.R

data class BlogPost(
    val id: Int,
    val date: String,
    @SerializedName("title") val title: Title,
    @SerializedName("excerpt") val excerpt: Excerpt,
    @SerializedName("jetpack_featured_media_url") val featuredMediaUrl: String,
    @SerializedName("link") val link: String,

)
data class Title(
    @SerializedName("rendered") val rendered: String
)

data class Excerpt(
    @SerializedName("rendered") val rendered: String
)

data class Rendered(
    val rendered: String
)


fun mapBlogPostToPost(blogPost: BlogPost): Post {
    return Post(
        title = blogPost.title.rendered,  // Extracting rendered title
        description = blogPost.excerpt.rendered,  // Extracting rendered excerpt
        image = R.drawable.image1,
        views = 0,  // Default or fetched value
        author = "Unknown Author",  // Replace with actual author info
        logo_of_author = R.drawable.ic_woof_logo,
        likes_no = 0,  // Default likes count
        comments = 0,  // Default comments count
        blogPost = blogPost,  // Direct reference
        rendered = Rendered(blogPost.title.rendered)  // Wrapping title in Rendered
    )
}