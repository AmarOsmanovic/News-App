package com.example.newsapp.screens

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.newsapp.R
import com.example.newsapp.data.local.NewsEntity
import com.example.newsapp.viewmodel.NewsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavController, newsId: String?, viewModel: NewsViewModel = viewModel()) {
    val newsList = viewModel.newsList.collectAsState().value
    var news by remember { mutableStateOf<NewsEntity?>(null) }

    LaunchedEffect(newsId) {
        if (newsId != null) {
            news = newsList.find { it.id == newsId.toInt() }
            if (news == null) {
                viewModel.getBreakingNews("general")
                news = newsList.find { it.id == newsId.toInt() }
            }
        }
    }

    news?.let {
        val context = LocalContext.current

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "News", fontWeight = FontWeight.Bold,color = Color.White) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                val isCurrentlySaved = news?.isSaved ?: false
                                if (isCurrentlySaved) {
                                    viewModel.unsaveNews(it.id)
                                } else {
                                    viewModel.saveNews(it)
                                    viewModel.setSavedValue(it.id)
                                }
                                news = news?.copy(isSaved = !isCurrentlySaved)
                                Log.d("DetailScreen", "Saved state: $news")
                            }
                        ) {
                            Icon(
                                imageVector = if (news?.isSaved == true) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "Save"
                            )
                        }

                        IconButton(onClick = {
                            val shareText = "${it.title}\n\nRead more: ${it.url}\n\nShared via NewsApp"
                            val shareIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, shareText)
                                type = "text/plain"
                            }
                            val chooser = Intent.createChooser(shareIntent, "Share via")
                            context.startActivity(chooser)
                        }) {
                            Icon(imageVector = Icons.Default.Share, contentDescription = "Share")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colorResource(id = R.color.light_blue_40),
                        navigationIconContentColor = Color.White,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        actionIconContentColor = Color.White
                    )
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = it.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                ) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color.Black)
                    )
                }
                Text(text = "Author: ${it.author ?: "Unknown"}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Text(text = "Source: ${it.source}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Spacer(modifier = Modifier.height(10.dp))
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current).data(data = it.imageUrl)
                            .apply(block = fun ImageRequest.Builder.() {
                                crossfade(true)
                                placeholder(R.drawable.image_not_available)
                            }).build()
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .height(300.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(20.dp))
                it.description?.let { description ->
                    Text(text = description, style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(45.dp))
                }
                Button(
                    modifier = Modifier
                        .width(150.dp)
                        .align(Alignment.End),
                    onClick = {
                        val encodedUrl = Uri.encode(it.url)
                        navController.navigate("webview?url=$encodedUrl")
                    }
                ) {
                    Text(text = "Read More")
                    Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
