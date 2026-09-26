package com.shaaya.kmpdailypluse.data.repository

import com.shaaya.kmpdailypluse.core.network.NetworkResult
import com.shaaya.kmpdailypluse.data.db.ArticlesDataSource
import com.shaaya.kmpdailypluse.data.remote.ArticlesApiService
import com.shaaya.kmpdailypluse.domain.model.Article
import com.shaaya.kmpdailypluse.domain.repository.ArticlesRepository

class ArticlesRepositoryImpl(
    private val apiService: ArticlesApiService,
    private val dataSource: ArticlesDataSource
) : ArticlesRepository {

    override suspend fun getArticles(): Result<List<Article>> {
        // 1. Read cached articles from local SQLDelight database
        val cachedArticles = dataSource.getAllArticles()

        // 2. Fetch fresh articles from network API
        return when (val result = apiService.fetchArticles()) {
            is NetworkResult.Success -> {
                val freshArticles = result.data.map {
                    Article(
                        title = it.title ?: "",
                        description = it.description ?: "",
                        imageUrl = it.urlToImage ?: "",
                        content = it.content ?: "",
                        publishedAt = it.publishedAt ?: ""
                    )
                }

                // 3. Update local database cache transactionally
                if (freshArticles.isNotEmpty()) {
                    dataSource.clearArticles()
                    dataSource.insertArticles(freshArticles)
                }

                Result.success(freshArticles)
            }

            is NetworkResult.Error -> {
                // 4. Fallback to cached articles if network request fails (Offline-First!)
                if (cachedArticles.isNotEmpty()) {
                    Result.success(cachedArticles)
                } else {
                    Result.failure(result.error.toException())
                }
            }
        }
    }
}
