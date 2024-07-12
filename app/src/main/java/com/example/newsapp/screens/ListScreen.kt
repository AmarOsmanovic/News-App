package com.example.newsapp.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.newsapp.viewmodel.NewsViewModel
import com.example.newsapp.R
import com.example.newsapp.data.local.NewsEntity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(navController: NavController, viewModel: NewsViewModel) {
    val configuration = LocalConfiguration.current
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val categories = listOf("Bookmarked", "General", "Business", "Health", "Science", "Sports", "Technology", "Entertainment")
    val newsList by viewModel.newsList.collectAsState()


    LaunchedEffect(selectedCategory) {
        if (selectedCategory == "Bookmarked") {
            viewModel.getSavedNews()
        } else {
            viewModel.getBreakingNews(selectedCategory.lowercase())
        }
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "News App",
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(colorResource(id = R.color.light_blue_40))
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp),
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colorResource(id = R.color.light_blue_40),
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
                ScrollableTabRow(
                    selectedTabIndex = categories.indexOf(selectedCategory),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    categories.forEachIndexed { _, category ->
                        Tab(
                            selected = selectedCategory == category,
                            onClick = { viewModel.setSelectedCategory(category) },
                            text = { Text(category, fontSize = 12.sp, maxLines = 1) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        if (configuration.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT) {
            PortraitLayout(navController, newsList, padding)
        } else {
            LandscapeLayout(navController, newsList, padding)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PortraitLayout(navController: NavController, newsList: List<NewsEntity>, padding: PaddingValues) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp)
    ) {
        items(newsList) { news ->
            NewsCard(navController, news)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LandscapeLayout(navController: NavController, newsList: List<NewsEntity>, padding: PaddingValues) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(newsList) { news ->
            NewsCard(navController, news, modifier = Modifier.padding(8.dp))
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewsCard(navController: NavController, news: NewsEntity, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                navController.navigate("detail/${news.id}")
            },
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.blue_grey_80)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            val imageUrl = news.imageUrl ?: ""
            Image(
                painter = if (imageUrl.isNotEmpty()) {
                    rememberAsyncImagePainter(imageUrl)
                } else {
                    painterResource(R.drawable.image_not_available)
                },
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp)),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = news.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            news.description?.let {
                Text(text = it, style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(8.dp))
            }
            val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
            val publishedDate = LocalDateTime.parse(news.publishedAt, DateTimeFormatter.ISO_DATE_TIME)
            Text(
                text = publishedDate.format(formatter),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.End),
                color = Color.DarkGray
            )
        }
    }
}
