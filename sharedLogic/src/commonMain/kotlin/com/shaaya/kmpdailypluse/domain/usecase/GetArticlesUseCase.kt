package com.shaaya.kmpdailypluse.domain.usecase

import com.shaaya.kmpdailypluse.core.util.formatRelativeDate
import com.shaaya.kmpdailypluse.domain.model.Article
import com.shaaya.kmpdailypluse.domain.repository.ArticlesRepository

class GetArticlesUseCase(private val repository: ArticlesRepository) {

    suspend operator fun invoke(): Result<List<Article>> {
        return repository.getArticles().map { articles ->
            articles.map { article ->
                article.copy(
                    publishedAt = formatRelativeDate(article.publishedAt)
                )
            }
        }
    }
}
