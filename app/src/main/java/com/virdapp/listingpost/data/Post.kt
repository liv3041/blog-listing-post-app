package com.virdapp.listingpost.data

import android.util.Log
import androidx.annotation.DrawableRes
import com.virdapp.listingpost.R
import com.virdapp.listingpost.remote.BlogApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

public final data  class AuthorDetails (
    var author: String = "",
    var avatar: String = ""
)

data class Post(

    val title: String,
    val description: String,
    @DrawableRes val image: Int,
    val views: Int,
    private var author:String = "",
    private var logo_of_author: String = "",
    val likes_no: Int,
    val comments:Int,
    val blogPost: BlogPost,
    val rendered: Rendered,
    var author_id: Int = 0,
    var authorDetails: AuthorDetails
) {
    init {
        fetchAuthorDetails(author_id)
    }
    private fun fetchAuthorDetails(authorId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = BlogApi.instance.getUser(authorId.toString())

                if (response.isSuccessful) {
                    response.body()?.let { authorData ->
                        withContext(Dispatchers.Main) { // UI update in main thread
                            author = authorData.name
                            logo_of_author = authorData.avatar_urls.`48`
                            authorDetails = AuthorDetails(author, logo_of_author)
                            Log.e("post-data-1", "fetchAuthorDetails: Author = $author, Logo = $logo_of_author")
                        }
                    }
                } else {
                    Log.e("post-data-1", "API Response Failed: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("post-data-1", "API Call Failed: ${e.message}")
            }
        }
    }


}
