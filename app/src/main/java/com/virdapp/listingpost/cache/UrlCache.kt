package com.virdapp.listingpost.cache

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "url_cache")
data class UrlCache(
    @PrimaryKey val url: String,
    val response: String,
    val timestamp: Long
) {

}
