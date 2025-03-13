package com.virdapp.listingpost.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UrlCacheDao {

    @Query("SELECT * FROM url_cache WHERE url = :url LIMIT 1")
    suspend fun getCachedResponse(url: String): UrlCache?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCache(urlCache: UrlCache)

    @Query("DELETE FROM url_cache WHERE timestamp < :expiryTime")
    suspend fun clearOldCache(expiryTime: Long)
}
