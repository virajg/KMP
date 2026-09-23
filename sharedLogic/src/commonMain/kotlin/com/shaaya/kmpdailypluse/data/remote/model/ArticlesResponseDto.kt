package com.shaaya.kmpdailypluse.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArticlesResponseDto(
    @SerialName("status")
    val status: String? = null,
    @SerialName("code")
    val code: String? = null,
    @SerialName("message")
    val message: String? = null,
    @SerialName("totalResults")
    val result: Int? = null,
    @SerialName("articles")
    val articles: List<ArticleDto>? = null
)
