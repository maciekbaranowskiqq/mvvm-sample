package com.spyro.network.api

import com.spyro.network.api.model.ArticleApiModel
import com.spyro.network.api.model.ArticleDetailsWrapper
import com.spyro.network.api.model.ArticlesListWrapper
import com.spyro.network.common.ApiCallResult
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class BackOfficeNetworkImplTest {
    private lateinit var subject: BackOfficeNetworkImpl

    @RelaxedMockK
    private lateinit var mockedApiService: BackOfficeApiService

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        subject = BackOfficeNetworkImpl(
            apiService = mockedApiService
        )
    }

    @Test
    fun `gets article details`() = runBlockingTest {

        val sampleArticleApiModel = ArticleApiModel(
            id = 1,
            title = "1",
            subtitle = "1",
            body = "1",
            date = "1",
        )

        val sampleArticleWrapper = ArticleDetailsWrapper(sampleArticleApiModel)

        coEvery { mockedApiService.getArticleDetails(1) } returns sampleArticleWrapper

        assertEquals(ApiCallResult.Success(sampleArticleWrapper), subject.getArticleDetails(1))
    }

    @Test
    fun `gets articles`() = runBlockingTest {

        val sampleArticleApiModel = ArticleApiModel(
            id = 1,
            title = "1",
            subtitle = "1",
            body = "1",
            date = "1",
        )

        val sampleArticleWrapper = ArticlesListWrapper(listOf(sampleArticleApiModel, sampleArticleApiModel))

        coEvery { mockedApiService.getArticles() } returns sampleArticleWrapper

        assertEquals(ApiCallResult.Success(sampleArticleWrapper), subject.getArticles())
    }
}
