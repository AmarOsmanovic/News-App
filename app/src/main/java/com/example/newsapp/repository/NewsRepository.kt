    package com.example.newsapp.repository

    import android.util.Log
    import com.example.newsapp.data.local.NewsDao
    import com.example.newsapp.data.local.NewsEntity
    import com.example.newsapp.data.remote.ApiClient
    import kotlinx.coroutines.flow.Flow
    import kotlinx.coroutines.flow.flow

    class NewsRepository(private val newsDao: NewsDao) {

        suspend fun getBreakingNews(category: String): Flow<List<NewsEntity>> {
            return flow {
                val response = ApiClient.apiService.getBreakingNews(category)
                val newsEntities = response.articles.map { article ->
                    NewsEntity(
                        id = article.url.hashCode(),
                        title = article.title,
                        description = article.description ?: "",
                        imageUrl = article.urlToImage ?: "",
                        publishedAt = article.publishedAt,
                        author = article.author,
                        source = article.source.name,
                        url = article.url
                    )
                }
                emit(newsEntities)
            }
        }

        suspend fun saveNews(news: NewsEntity) {
            try {
                newsDao.saveNews(news)
            } catch (e: Exception) {
                Log.e("NewsRepository", "Exception occurred while saving news", e)
            }
        }

        suspend fun setSavedValue(newsId: Int) {
            try {
                newsDao.setSavedValue(newsId)
            } catch (e: Exception) {
                Log.e("NewsRepository", "Exception occurred while saving news", e)
            }
        }

        suspend fun unsaveNews(newsId: Int) {
            try {
                newsDao.unsaveNews(newsId)
            } catch (e: Exception) {
                Log.e("NewsRepository", "Exception occurred while unsaving news", e)
            }
        }

        fun getSavedNews(): Flow<List<NewsEntity>> {
            return try {
                newsDao.getSavedNews()
            } catch (e: Exception) {
                throw e
            }
        }
    }