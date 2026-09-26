package com.shaaya.kmpdailypluse.data.db

import com.shaaya.kmpdailypluse.db.DailyPulseDatabase
import com.shaaya.kmpdailypluse.domain.model.Article

class ArticlesDataSource(private val database: DailyPulseDatabase) {

    fun getAllArticles(): List<Article> {
        return database.articlesDatabaseQueries.selectAllArticles { title, description, content, imageUrl, publishedAt ->
            Article(
                title = title,
                description = description,
                content = content,
                imageUrl = imageUrl,
                publishedAt = publishedAt
            )
        }.executeAsList()
    }

    fun insertArticles(articles: List<Article>) {
        database.articlesDatabaseQueries.transaction {
            articles.forEach { article ->
                insertArticle(article)
            }
        }
    }

    fun clearArticles() {
        database.articlesDatabaseQueries.removeAllArticles()
    }

    private fun insertArticle(article: Article) {
        database.articlesDatabaseQueries.insertArticle(
            title = article.title,
            description = article.description,
            content = article.content,
            imageUrl = article.imageUrl,
            publishedAt = article.publishedAt
        )
    }
}
