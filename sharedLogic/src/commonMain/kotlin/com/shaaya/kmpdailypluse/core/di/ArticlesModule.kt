package com.shaaya.kmpdailypluse.core.di

import com.shaaya.kmpdailypluse.data.db.ArticlesDataSource
import com.shaaya.kmpdailypluse.data.repository.ArticlesRepositoryImpl
import com.shaaya.kmpdailypluse.domain.repository.ArticlesRepository
import com.shaaya.kmpdailypluse.domain.usecase.GetArticlesUseCase
import com.shaaya.kmpdailypluse.presentation.articles.ArticlesViewModel
import org.koin.dsl.module

val articlesModule = module {
    single { ArticlesDataSource(get()) }
    single<ArticlesRepository> { ArticlesRepositoryImpl(get(), get()) }
    factory { GetArticlesUseCase(get()) }
    factory { ArticlesViewModel(get()) }
}
