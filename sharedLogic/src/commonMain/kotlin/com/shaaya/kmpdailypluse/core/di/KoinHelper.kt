package com.shaaya.kmpdailypluse.core.di

import com.shaaya.kmpdailypluse.presentation.articles.ArticlesViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class KoinHelper : KoinComponent {
    val articlesViewModel: ArticlesViewModel by inject()
}
