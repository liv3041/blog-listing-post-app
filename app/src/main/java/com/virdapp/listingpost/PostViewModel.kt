package com.virdapp.listingpost

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.virdapp.listingpost.data.BlogPost
import com.virdapp.listingpost.data.Post
import com.virdapp.listingpost.data.Rendered
import com.virdapp.listingpost.data.mapBlogPostToPost
import com.virdapp.listingpost.remote.BlogApi
import com.virdapp.listingpost.remote.BlogApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PostViewModel : ViewModel() {

    // Holds the list of fetched posts
    var data = mutableStateOf<List<Post>>(emptyList())

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

        _posts.value = _posts.value + newPostList // Append new posts
        Log.e("post-data", "processNewPosts: ${currentPage}")
        currentPage++ // Move to the next page
    }

    private fun handleApiError(e: Exception) {
        _errorMessage.value = e.localizedMessage ?: "Something went wrong"
    }
}
