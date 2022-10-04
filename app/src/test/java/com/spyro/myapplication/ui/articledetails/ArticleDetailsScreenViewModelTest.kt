package com.spyro.myapplication.ui.articledetails

import com.spyro.myapplication.R
import com.spyro.myapplication.ui.articledetails.ArticleDetailsScreenInteractions.ScreenEntered
import com.spyro.myapplication.utility.InstantTaskExecutionTest
import com.spyro.myapplication.utility.StringResourceProvider
import com.spyro.myapplication.utility.UiMessagePresenter
import com.spyro.network.api.BackOfficeNetwork
import com.spyro.network.api.model.ArticleApiModel
import com.spyro.network.api.model.ArticleDetailsWrapper
import com.spyro.network.common.ApiCallResult
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ArticleDetailsScreenViewModelTest : InstantTaskExecutionTest() {
    private lateinit var subject: ArticleDetailsScreenViewModel

    @RelaxedMockK
    private lateinit var mockedUiMessagePresenter: UiMessagePresenter

    @RelaxedMockK
    private lateinit var mockedBackOfficeNetwork: BackOfficeNetwork

    @RelaxedMockK
    private lateinit var mockedStringResourceProvider: StringResourceProvider

    @Before
    override fun setUp() {
        super.setUp()
        MockKAnnotations.init(this)

        subject = ArticleDetailsScreenViewModel(
            uiMessagePresenter = mockedUiMessagePresenter,
            stringResourceProvider = mockedStringResourceProvider,
            backOfficeNetwork = mockedBackOfficeNetwork
        )
    }

    @Test
    fun `while requesting article details, shows progress indicator`() {
        coEvery { mockedBackOfficeNetwork.getArticleDetails(1) } returns ApiCallResult.Failure(Throwable())

        testCoroutineDispatcher.pauseDispatcher()
        subject.onInteraction(ScreenEntered(1))
        assertEquals(true, subject.isProgressIndicatorVisibleLiveData.value)

        testCoroutineDispatcher.resumeDispatcher()
        assertEquals(false, subject.isProgressIndicatorVisibleLiveData.value)
    }

    @Test
    fun `when screen entered, requests article details`() {
        subject.onInteraction(ScreenEntered(1))

        coVerify { mockedBackOfficeNetwork.getArticleDetails(1) }
    }

    @Test
    fun `when request for articles is canceled, shows toast message`() {
        coEvery { mockedBackOfficeNetwork.getArticleDetails(1) } returns ApiCallResult.Cancelled(Throwable("Sample Error"))
        coEvery { mockedStringResourceProvider.getString(R.string.api_request_cancelled) } returns "Request cancelled."

        subject.onInteraction(ScreenEntered(1))

        verify { mockedUiMessagePresenter.showToastMessage("Request cancelled.") }
    }

    @Test
    fun `when request for articles fails, shows toast message`() {
        coEvery { mockedBackOfficeNetwork.getArticleDetails(1) } returns ApiCallResult.Failure(Throwable("Sample Error"))
        coEvery { mockedStringResourceProvider.getString(R.string.api_request_failure) } returns "Failed to fetch articles."

        subject.onInteraction(ScreenEntered(1))

        verify { mockedUiMessagePresenter.showToastMessage("Failed to fetch articles.") }
    }

    @Test
    fun `when request for articles is successful, render articles`() {
        val sampleArticlesRaw = ArticleDetailsWrapper(ArticleApiModel(1, "1", "1", "1", "1"))
        val sampleArticlesUi = ArticleDetailsScreenViewModel.ArticleDetailsUiModel(1, "1", "1", "1", "1")

        coEvery { mockedBackOfficeNetwork.getArticleDetails(1) } returns ApiCallResult.Success(sampleArticlesRaw)

        subject.onInteraction(ScreenEntered(1))

        assertEquals(sampleArticlesUi, subject.articleDetailsLiveData.value)
    }
}
