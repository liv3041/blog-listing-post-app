package com.virdapp.listingpost.data

import android.icu.text.CaseMap
import android.util.Log
import com.google.gson.annotations.SerializedName
import com.virdapp.listingpost.R
import com.virdapp.listingpost.data.author_data.Author
import com.virdapp.listingpost.remote.BlogApi

data class BlogPost(
    val id: Int,
    val date: String,
    @SerializedName("title") val title: Title,
    @SerializedName("excerpt") val excerpt: Excerpt,
    @SerializedName("jetpack_featured_media_url") val featuredMediaUrl: String,
    @SerializedName("link") val link: String,
    val author: Int,
    @SerializedName("jetpack_shortlink") val shareLink: String

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
//    val author = BlogApi.instance.getUser(blogPost.author.toString())
//    Log.e("BLOG_POST", "mapBlogPostToPost: ${author.name}")
    var post = Post(
        title = blogPost.title.rendered,  // Extracting rendered title
        description = blogPost.excerpt.rendered,  // Extracting rendered excerpt
        image = R.drawable.image1,
        views = 0,  // Default or fetched value
        author = "",  // Replace with actual author info
        logo_of_author = "",
        likes_no = 0,  // Default likes count
        comments = 0,  // Default comments count
        blogPost = blogPost,  // Direct reference
        rendered = Rendered(blogPost.title.rendered),
        author_id = blogPost.author,
        authorDetails = AuthorDetails(),
        shareLink = blogPost.shareLink
    )
    return post
}