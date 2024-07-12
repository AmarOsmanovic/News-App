package com.example.newsapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news")
data class NewsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String?,
    val imageUrl: String?,
    val publishedAt: String,
    val author: String?,
    val source: String?,
    val url: String,
    var isSaved: Boolean = false
)
