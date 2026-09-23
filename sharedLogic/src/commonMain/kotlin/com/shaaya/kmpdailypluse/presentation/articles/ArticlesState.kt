package com.shaaya.kmpdailypluse.presentation.articles

import com.shaaya.kmpdailypluse.domain.model.Article

sealed class ArticlesState {
    data object Loading : ArticlesState()

    data class Success(
        val items: List<Article>
    ) : ArticlesState()

    data object Empty : ArticlesState()

    data class Error(
        val message: String
    ) : ArticlesState()
}
