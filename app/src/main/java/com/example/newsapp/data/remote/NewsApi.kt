package com.example.newsapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("top-headlines")
    suspend fun getBreakingNews(
        @Query("category") category: String,
        @Query("country") country: String = "us",
        @Query("apiKey") apiKey: String = API_KEY
    ): NewsResponse

    companion object {
        const val API_KEY = "7cdac76b75b848969e2274fcbc9349a9"
        const val BASE_URL = "https://newsapi.org/v2/"
    }
}
