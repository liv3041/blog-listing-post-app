package com.virdapp.listingpost.data.author_data

import com.google.gson.annotations.SerializedName

data class Author(
    val links: Links,
    @SerializedName("avatar_urls")
    val avatar_urls: AvatarUrls,
    val description: String,
    val id: Int,
    val link: String,
    val meta: Meta,
    var name: String,
    val slug: String,
    val url: String

)
data class Meta(
    val jetpack_donation_warning_dismissed: Boolean
)

