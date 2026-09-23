package com.shaaya.kmpdailypluse.data.repository

import com.shaaya.kmpdailypluse.core.network.NetworkResult
import com.shaaya.kmpdailypluse.data.remote.ArticlesApiService
import com.shaaya.kmpdailypluse.domain.model.Article
import com.shaaya.kmpdailypluse.domain.repository.ArticlesRepository

class ArticlesRepositoryImpl(
    private val apiService: ArticlesApiService
) : ArticlesRepository {

    override suspend fun getArticles(): Result<List<Article>> {
        return when (val result = apiService.fetchArticles()) {
            is NetworkResult.Success -> {
                val articles = result.data.map {
                    Article(
                        title = it.title ?: "",
                        description = it.description ?: "",
                        imageUrl = it.urlToImage ?: "",
                        content = it.content ?: "",
                        publishedAt = it.publishedAt ?: ""
                    )
                }
                Result.success(articles)
            }
            is NetworkResult.Error -> {
                Result.failure(result.error.toException())
            }
        }
    }
}
