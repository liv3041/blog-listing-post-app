package com.virdapp.listingpost

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.virdapp.listingpost.cache.PostRepository
import com.virdapp.listingpost.data.BlogPost
import com.virdapp.listingpost.data.Post
import com.virdapp.listingpost.data.author_data.AuthorDetails
import com.virdapp.listingpost.data.mapBlogPostToPost
import com.virdapp.listingpost.remote.BlogApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PostViewModel : ViewModel() {

    // Holds the list of fetched posts
    private val _posts = mutableStateOf<List<Post>>(emptyList())
    val posts = _posts

    // Loading state
    private val _isLoading = mutableStateOf(false)
    val isLoading = _isLoading

    // Error state (Changed to MutableState<String?>)
    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage = _errorMessage

    // Pagination variables
    var currentPage = 1
    private val pageSize = 10
    private var isLastPage = false



    init {
//        Log.e("FetchAuthorDetails", "AuthorId:${post.author_id}: ", )

        getPosts()

    }

    fun getPosts() {

        viewModelScope.launch {
            fetchPosts()
        }


    }

    private suspend fun fetchPosts() {
        Log.e("post-data", "fetchPosts: ${currentPage}")
        try {
            _isLoading.value = true

            val postList = fetchPostsFromApi(currentPage, pageSize)

            if (postList.isNotEmpty()) {
                processNewPosts(postList)
            } else {
                isLastPage = true // Stop fetching if no more data
            }

            _errorMessage.value = null
        } catch (e: Exception) {
            handleApiError(e)
        } finally {
            _isLoading.value = false
        }
    }

    private suspend fun fetchPostsFromApi(page: Int, pageSize: Int): List<BlogPost> {
        return BlogApi.instance.getPosts(page = page, perPage = pageSize)
    }

    private fun processNewPosts(postList: List<BlogPost>) {
        val newPostList = postList.map { mapBlogPostToPost(it) }
        _posts.value = _posts.value + newPostList

        newPostList.forEach { post ->
            fetchAuthorDetails(post)
        }

        currentPage++
    }


    private fun handleApiError(e: Exception) {
        _errorMessage.value = e.localizedMessage ?: "Something went wrong"
    }


    private fun fetchAuthorDetails(post: Post) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = BlogApi.instance.getUser(post.author_id.toString())
                Log.e("post-data-1", "AuthorId:${post.author_id}: ", )
                if (response.isSuccessful) {
                    response.body()?.let { authorData ->
                        withContext(Dispatchers.Main) {
                            post.authorDetails = listOf(
                                AuthorDetails(authorData.name, authorData.avatar_urls.`24`)
                            )
                            Log.e("post-data-1", "Author: ${authorData.name}, Logo: ${authorData.avatar_urls.`24`}")
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

