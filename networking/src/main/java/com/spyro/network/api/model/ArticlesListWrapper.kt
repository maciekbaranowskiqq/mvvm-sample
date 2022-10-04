package com.spyro.network.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ArticlesListWrapper(
    @Json(name = "items")
    val articles: List<ArticleApiModel>,
)
