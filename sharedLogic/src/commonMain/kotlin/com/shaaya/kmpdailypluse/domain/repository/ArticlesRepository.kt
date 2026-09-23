package com.shaaya.kmpdailypluse.domain.repository

import com.shaaya.kmpdailypluse.domain.model.Article

interface ArticlesRepository {
    suspend fun getArticles(): Result<List<Article>>
}
