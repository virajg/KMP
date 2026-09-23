package com.shaaya.kmpdailypluse.data.remote

import com.shaaya.kmpdailypluse.core.network.NetworkResult
import com.shaaya.kmpdailypluse.core.network.safeApiCall
import com.shaaya.kmpdailypluse.data.remote.model.ArticleDto
import com.shaaya.kmpdailypluse.data.remote.model.ArticlesResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class ArticlesApiService(private val httpClient: HttpClient) {
    companion object {
        private const val API_KEY = "df9f3ff2abf849468ceb066af53a20bd"
        private const val BASE_URL = "https://newsapi.org/v2/top-headlines"
    }

    suspend fun fetchArticles(): NetworkResult<List<ArticleDto>> {
        return safeApiCall {
            val response = httpClient
                .get(BASE_URL) {
                    parameter("country", "us")
                    parameter("apiKey", API_KEY)
                }.body<ArticlesResponseDto>()

            if (response.status == "error") {
                throw Exception(response.message ?: response.code ?: "NewsAPI Error")
            }

            response.articles ?: emptyList()
        }
    }
}
