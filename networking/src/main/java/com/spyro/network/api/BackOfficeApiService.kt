package com.spyro.network.api

import com.spyro.network.api.model.ArticleDetailsWrapper
import com.spyro.network.api.model.ArticlesListWrapper
import retrofit2.http.GET
import retrofit2.http.Path

internal interface BackOfficeApiService {
    @GET("/test/native/contentList.json")
    suspend fun getArticles(): ArticlesListWrapper

    @GET("/test/native/content/{id}.json")
    suspend fun getArticleDetails(@Path("id") user: Long): ArticleDetailsWrapper
}
