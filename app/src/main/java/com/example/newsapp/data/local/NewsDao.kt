package com.example.newsapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNews(news: NewsEntity)

    @Query("UPDATE news SET isSaved = 1 WHERE id = :newsId")
    suspend fun setSavedValue(newsId: Int)

    @Query("DELETE FROM news WHERE id = :newsId")
    suspend fun unsaveNews(newsId: Int)

    @Query("SELECT * FROM news WHERE isSaved = 1")
    fun getSavedNews(): Flow<List<NewsEntity>>

    @Query("SELECT * FROM news")
    fun getAllNews(): Flow<List<NewsEntity>>
}
