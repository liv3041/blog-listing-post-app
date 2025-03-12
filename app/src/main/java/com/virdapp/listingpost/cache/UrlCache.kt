package com.virdapp.listingpost.cache

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "url_cache")
data class UrlCache(
    @PrimaryKey val url: String, // Use URL as the primary key
    val response: String, // Store the response as a String
    val timestamp: Long // Store time to manage cache expiration
) {

}
