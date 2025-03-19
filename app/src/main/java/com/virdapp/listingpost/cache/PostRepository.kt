package com.virdapp.listingpost.cache

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class PostRepository(context: Context) {
    private val cacheDao = PostDatabase.getDatabase(context).urlCacheDao()
    private val cacheExpiry = 10 * 60 * 1000 // 10 minutes

    suspend fun getResponse(url: String): String {
        return withContext(Dispatchers.IO) {
            val cachedData = cacheDao.getCachedResponse(url)
            val currentTime = System.currentTimeMillis()

            if (cachedData != null && (currentTime - cachedData.timestamp) < cacheExpiry) {
                return@withContext cachedData.response
            }

            val response = fetchFromNetwork(url)
            cacheDao.insertCache(UrlCache(url, response, currentTime))
            response
        }
    }

    private fun fetchFromNetwork(urlString: String): String {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        return connection.inputStream.bufferedReader().use { it.readText() }
    }


}
