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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PostViewModel:ViewModel() {

    var data = mutableStateOf<List<Post>>(emptyList())
    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)

    private var page = 1
    private val pageSize = 10  // Load 10 posts per page
    private var allDataLoaded = false


    init {
        Log.d("api-result","init called")
        getPosts()
//        loadPosts()
    }


    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> get() = _posts

    fun loadPosts() {
        if (isLoading.value || allDataLoaded) return

        isLoading.value = true

        viewModelScope.launch {
            delay(1000)  // Simulating network delay

            val newPosts = fetchPosts(
                page, pageSize,
                blogPost = TODO()
            )
            if (newPosts.isEmpty()) {
                allDataLoaded = true
            } else {
                data.value = data.value + newPosts
                page++
            }

            isLoading.value = false
        }
    }
    private fun fetchPosts(page: Int, pageSize: Int, blogPost: BlogPost): List<Post> {
        // Replace this with your API fetching logic
        return List(pageSize) { index ->
            Post(
                title = blogPost.title.rendered,  // Extracting rendered title
                description = blogPost.excerpt.rendered,  // Extracting rendered excerpt
                image = R.drawable.image1,
                views = 0,  // Default or fetched value
                author = "Unknown Author",  // Replace with actual author info
                logo_of_author = R.drawable.ic_woof_logo,
                likes_no = 0,  // Default likes count
                comments = 0,  // Default comments count
                blogPost = blogPost,  // Direct reference
                rendered = Rendered(blogPost.title.rendered))
        }
    }

    fun getPosts() {

        viewModelScope.launch {
            try {
                val postList = BlogApi.instance.getPosts()
                Log.d("api-result",postList.toString())
                val newPostList = postList.map { blogPost ->
                    mapBlogPostToPost(blogPost)
                }

                _posts.value = newPostList
                println()
                Log.d("api-result",newPostList.toString())
                data.value = newPostList
                errorMessage.value = null
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error: ${e.message}")
                errorMessage.value = e.localizedMessage ?: "Something went wrong"
            }
        }
    }

}