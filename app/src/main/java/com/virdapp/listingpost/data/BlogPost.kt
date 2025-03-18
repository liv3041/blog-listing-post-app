package com.virdapp.listingpost.data

import com.google.gson.annotations.SerializedName
import com.virdapp.listingpost.R
import com.virdapp.listingpost.data.author_data.AuthorDetails

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
        title = blogPost.title.rendered,
        description = blogPost.excerpt.rendered,
        image = R.drawable.image1__placeholderpng,
        views = 0,
        author = "",
        logo_of_author = "",
        likes_no = 0,
        comments = 0,
        blogPost = blogPost,
        rendered = Rendered(blogPost.title.rendered),
        author_id = blogPost.author,
        authorDetails = listOf(AuthorDetails()) ,
        shareLink = blogPost.shareLink,
        featuredMediaUrl = blogPost.featuredMediaUrl,
        link = blogPost.link
    )
    return post
}