package com.spyro.network.api

import com.spyro.network.api.model.ArticleDetailsWrapper
import com.spyro.network.api.model.ArticlesListWrapper
import com.spyro.network.common.ApiCallResult
import com.spyro.network.common.executeApiCallSafely
import javax.inject.Inject

interface BackOfficeNetwork {

    suspend fun getArticles(): ApiCallResult<ArticlesListWrapper>

    suspend fun getArticleDetails(id: Long): ApiCallResult<ArticleDetailsWrapper>
}

internal class BackOfficeNetworkImpl @Inject constructor(
    private val apiService: BackOfficeApiService,
) : BackOfficeNetwork {
    override suspend fun getArticles() =
        executeApiCallSafely {
            apiService.getArticles()
        }

    override suspend fun getArticleDetails(id: Long) =
        executeApiCallSafely {
            apiService.getArticleDetails(id)
        }
}
