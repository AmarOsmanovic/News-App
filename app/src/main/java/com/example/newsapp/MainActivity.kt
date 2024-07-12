package com.example.newsapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.newsapp.data.local.NewsDatabase
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.screens.Navigation
import com.example.newsapp.ui.theme.NewsAppTheme
import com.example.newsapp.viewmodel.NewsViewModel
import com.example.newsapp.viewmodel.NewsViewModelFactory

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val newsDao = NewsDatabase.getDatabase(applicationContext).newsDao()
        val repository = NewsRepository(newsDao)
        val newsViewModel: NewsViewModel by lazy {
            ViewModelProvider(this, NewsViewModelFactory(repository)).get(NewsViewModel::class.java)
        }

        setContent {
            NewsAppTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    Navigation(navController = navController, newsViewModel = newsViewModel)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NewsAppTheme {
        val navController = rememberNavController()
        val context = LocalContext.current
        val newsDao = NewsDatabase.getDatabase(context).newsDao()
        val repository = NewsRepository(newsDao)
        val newsViewModel = NewsViewModel(repository)
        Navigation(navController = navController, newsViewModel = newsViewModel)
    }
}

