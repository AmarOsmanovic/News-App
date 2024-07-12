package com.example.newsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.local.NewsEntity
import com.example.newsapp.repository.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NewsViewModel(private val repository: NewsRepository) : ViewModel() {

    private val _newsList = MutableStateFlow<List<NewsEntity>>(emptyList())
    val newsList: StateFlow<List<NewsEntity>> = _newsList

    private val _selectedCategory = MutableStateFlow("General")
    val selectedCategory: StateFlow<String> = _selectedCategory

    init {
        if (selectedCategory.value == "Bookmarked") {
            getSavedNews()
        } else {
            getBreakingNews(selectedCategory.value.lowercase())
        }
    }


    fun setSelectedCategory(category: String) {
        _selectedCategory.value = category
        if (category == "Bookmarked") {
            getSavedNews()
        } else {
            getBreakingNews(category.lowercase())
        }
    }


    fun getBreakingNews(category: String) {
        viewModelScope.launch {
            repository.getBreakingNews(category).collect { news ->
                _newsList.value = news
            }
        }
    }

    fun saveNews(news: NewsEntity) {
        viewModelScope.launch {
            repository.saveNews(news)
        }
    }

    fun setSavedValue(newsId: Int) {
        viewModelScope.launch {
            repository.setSavedValue(newsId)
        }
    }

    fun unsaveNews(newsId: Int) {
        viewModelScope.launch {
            repository.unsaveNews(newsId)
        }
    }

    fun getSavedNews() {
        viewModelScope.launch {
            repository.getSavedNews().collect { savedNews ->
                _newsList.value = savedNews
            }
        }
    }



}
