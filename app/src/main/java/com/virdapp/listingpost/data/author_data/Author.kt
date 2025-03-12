package com.virdapp.listingpost.data.author_data

data class Author(
    val _links: Links,
    val avatar_urls: AvatarUrls,
    val description: String,
    val id: Int,
    val link: String,
    val meta: List<Any>,
    var name: String,
    val slug: String,
    val url: String
)