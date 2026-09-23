package com.shaaya.kmpdailypluse.presentation.articles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shaaya.kmpdailypluse.domain.usecase.GetArticlesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ArticlesViewModel(
    private val getArticlesUseCase: GetArticlesUseCase
) : ViewModel() {
    private val _articlesState: MutableStateFlow<ArticlesState> = MutableStateFlow(ArticlesState.Empty)
    val articlesState: StateFlow<ArticlesState> = _articlesState

    init {
        loadArticles()
    }

    fun loadArticles() {
        viewModelScope.launch {
            _articlesState.value = ArticlesState.Loading

            val result = getArticlesUseCase()

            result
                .onSuccess { articles ->
                    _articlesState.value = if (articles.isEmpty()) {
                        ArticlesState.Empty
                    } else {
                        ArticlesState.Success(articles)
                    }
                }
                .onFailure { exception ->
                    _articlesState.value = ArticlesState.Error(
                        exception.message ?: "Something went wrong"
                    )
                }
        }
    }
}
