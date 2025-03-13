package com.virdapp.listingpost.cache

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PostCacheViewModel (private val repository: PostRepository) : ViewModel() {
    fun fetchData(url: String, onResult: (String) -> Unit) {
        viewModelScope.launch {
            val result = repository.getResponse(url)
            onResult(result)
        }
    }
}