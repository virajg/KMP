package com.shaaya.kmpdailypluse.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.shaaya.kmpdailypluse.domain.model.Article
import com.shaaya.kmpdailypluse.presentation.articles.ArticlesState
import com.shaaya.kmpdailypluse.presentation.articles.ArticlesViewModel
import org.koin.compose.viewmodel.koinViewModel

/**
 * Container Composable: Handles ViewModel injection, lifecycle-aware state collection,
 * and navigation actions.
 */
@Composable
fun ArticleScreen(
    onAboutClick: () -> Unit = {},
    viewModel: ArticlesViewModel = koinViewModel()
) {
    val articlesState by viewModel.articlesState.collectAsStateWithLifecycle()

    ArticleScreenContent(
        state = articlesState,
        onRetry = { viewModel.loadArticles() },
        onAboutClick = onAboutClick
    )
}

/**
 * Stateless Presentation Composable: Renders UI based solely on state and events.
 * 100% testable and previewable without a ViewModel instance.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleScreenContent(
    state: ArticlesState,
    onRetry: () -> Unit,
    onAboutClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Articles") },
                actions = {
                    IconButton(onClick = onAboutClick) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = "About Screen"
                        )
                    }
                }
            )
        },
    ) { paddingValues ->
        when (state) {
            is ArticlesState.Loading -> {
                Loader(modifier = Modifier.padding(paddingValues))
            }
            is ArticlesState.Success -> {
                ArticleListView(
                    articles = state.items,
                    paddingValues = paddingValues
                )
            }
            is ArticlesState.Empty -> {
                ArticleEmptyView(
                    modifier = Modifier.padding(paddingValues),
                    onRetry = onRetry
                )
            }
            is ArticlesState.Error -> {
                ArticleErrorView(
                    errorMessage = state.message,
                    modifier = Modifier.padding(paddingValues),
                    onRetry = onRetry
                )
            }
        }
    }
}

@Composable
fun ArticleListView(
    articles: List<Article>,
    paddingValues: PaddingValues
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            items = articles,
            key = { article -> article.title }
        ) { article ->
            ArticleRowView(article = article)
        }
    }
}

@Composable
fun ArticleRowView(article: Article) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        if (article.imageUrl.isNotEmpty()) {
            AsyncImage(
                model = article.imageUrl,
                contentDescription = article.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        Text(
            text = article.title,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = article.description,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = article.publishedAt,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.align(Alignment.End)
        )
    }
}

@Composable
fun ArticleEmptyView(
    title: String = "No articles found",
    message: String = "There are no articles available right now.",
    modifier: Modifier = Modifier,
    onRetry: (() -> Unit)? = null
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Article,
                contentDescription = null
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            if (onRetry != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onRetry) {
                    Text(text = "Retry")
                }
            }
        }
    }
}

@Composable
fun ArticleErrorView(
    errorMessage: String,
    modifier: Modifier = Modifier,
    onRetry: (() -> Unit)? = null
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
            if (onRetry != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onRetry) {
                    Text(text = "Retry")
                }
            }
        }
    }
}

@Composable
fun Loader(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.width(32.dp),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

// ============================================================================
// Previews
// ============================================================================

@Preview
@Composable
fun ArticleScreenSuccessPreview() {
    MaterialTheme {
        ArticleScreenContent(
            state = ArticlesState.Success(
                items = listOf(
                    Article(
                        title = "Kotlin Multiplatform 2.0 Released",
                        description = "Discover the latest features in Kotlin Multiplatform.",
                        content = "",
                        imageUrl = "",
                        publishedAt = "2026-09-22"
                    ),
                    Article(
                        title = "Jetpack Compose Best Practices",
                        description = "Learn how to structure your Compose screens for scalability.",
                        content = "",
                        imageUrl = "",
                        publishedAt = "2026-09-21"
                    )
                )
            ),
            onRetry = {}
        )
    }
}

@Preview
@Composable
fun ArticleScreenLoadingPreview() {
    MaterialTheme {
        ArticleScreenContent(
            state = ArticlesState.Loading,
            onRetry = {}
        )
    }
}

@Preview
@Composable
fun ArticleScreenEmptyPreview() {
    MaterialTheme {
        ArticleScreenContent(
            state = ArticlesState.Empty,
            onRetry = {}
        )
    }
}

@Preview
@Composable
fun ArticleScreenErrorPreview() {
    MaterialTheme {
        ArticleScreenContent(
            state = ArticlesState.Error("Network connection failed. Please try again."),
            onRetry = {}
        )
    }
}
