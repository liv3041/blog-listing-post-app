import android.content.Context
import android.util.Log
import com.virdapp.listingpost.cache.PostDatabase
import com.virdapp.listingpost.cache.UrlCache
import com.virdapp.listingpost.cache.UrlCacheDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Response
import okhttp3.ResponseBody
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object  RespCacheObject{
    private lateinit var cacheDao:UrlCacheDao

    fun setContext(context: Context) {
        cacheDao = PostDatabase.getDatabase(context).urlCacheDao()
    }

    fun getCacheDao(): UrlCacheDao {
        return cacheDao
    }
}

class ResponseCacheInterceptor (
    private val cacheDao: UrlCacheDao
) : Interceptor {

    private val executor: ExecutorService = Executors.newSingleThreadExecutor()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // Only cache GET requests
        if (request.method != "GET") {
            return chain.proceed(request)
        }

        val cacheKey = request.url.toString()

        // Use a CompletableFuture or some other background task to handle suspending functions.
        val cachedResponse = getCachedResponse(cacheKey)

        return if (cachedResponse != null && isCacheValid(cachedResponse.timestamp))  {
            Log.d("ResponseCacheInterceptor", "picked cached - request: ${cacheKey} response: ${cachedResponse.response}")
            val cache = cachedResponse.response as String
            val responseBody: ResponseBody = ResponseBody.create("application/json".toMediaTypeOrNull(), cache)
            Response.Builder()
                .request(request)
                .code(200)
                .protocol(chain.connection()?.protocol() ?: okhttp3.Protocol.HTTP_1_1)
                .message("OK")
                .body(responseBody)
                .build()

        } else {
            // Proceed with network request and cache the response asynchronously
            val networkResponse = chain.proceed(request)
            Log.d("ResponseCacheInterceptor", "request: ${cacheKey} call api: ${networkResponse.body}")
            val responseString = networkResponse.body?.string() ?: ""
            cacheResponse(cacheKey, responseString)
            return networkResponse.newBuilder()
                .body(ResponseBody.create(networkResponse.body?.contentType(), responseString))
                .build()
        }
    }

    // Get the cached response from RoomDB using Executor to run the suspend function in the background
    private fun getCachedResponse(url: String): UrlCache? {
        return runBlocking {
            withContext(Dispatchers.IO) {
                cacheDao.getCachedResponse(url)
            }
        }
    }

    // Cache the network response in RoomDB (using suspend function)
    private fun cacheResponse(url: String, responseString: String) {
        executor.execute {
            val timestamp = System.currentTimeMillis()

            val cache = UrlCache(url = url, response = responseString, timestamp = timestamp)
            runBlocking {
                Log.d("ResponseCacheInterceptor", "cache-key ${url} insert-cacheResponse: $responseString")
                withContext(Dispatchers.IO) {
                    cacheDao.insertCache(cache)
                }
            }
        }
    }

    private fun isCacheValid(timestamp: Long): Boolean {
        val cacheDuration = 60 * 60 * 1000 // Cache for 1 hour
        return System.currentTimeMillis() - timestamp <= cacheDuration
    }
}
