package com.example.newsapp.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.newsapp.viewmodel.NewsViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(navController: NavHostController, newsViewModel: NewsViewModel) {
    NavHost(navController, startDestination = "intro") {
        composable("intro") {
            IntroScreen(navController)
        }
        composable("list") {
            ListScreen(navController, newsViewModel)
        }
        composable("detail/{newsId}") { backStackEntry ->
            DetailScreen(navController, backStackEntry.arguments?.getString("newsId"), newsViewModel)
        }
        composable("webview?url={url}") { backStackEntry ->

            val url = backStackEntry.arguments?.getString("url")
            WebViewScreen(navController, url)
        }
    }
}
