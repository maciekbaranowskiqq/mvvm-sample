package com.spyro.myapplication.ui.articleslist

import com.spyro.myapplication.R
import com.spyro.myapplication.ui.articleslist.ArticlesScreenInteractions.OnArticleItemClicked
import com.spyro.myapplication.ui.articleslist.ArticlesScreenInteractions.ScreenEntered
import com.spyro.myapplication.utility.InstantTaskExecutionTest
import com.spyro.myapplication.utility.StringResourceProvider
import com.spyro.myapplication.utility.UiMessagePresenter
import com.spyro.network.api.BackOfficeNetwork
import com.spyro.network.api.model.ArticleApiModel
import com.spyro.network.api.model.ArticlesListWrapper
import com.spyro.network.common.ApiCallResult
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ArticlesScreenViewModelTest : InstantTaskExecutionTest() {
    private lateinit var subject: ArticlesScreenViewModel

    @RelaxedMockK
    private lateinit var mockedUiMessagePresenter: UiMessagePresenter

    @RelaxedMockK
    private lateinit var mockedNavigator: ArticlesScreenNavigator

    @RelaxedMockK
    private lateinit var mockedBackOfficeNetwork: BackOfficeNetwork

    @RelaxedMockK
    private lateinit var mockedStringResourceProvider: StringResourceProvider

    @Before
    override fun setUp() {
        super.setUp()
        MockKAnnotations.init(this)

        subject = ArticlesScreenViewModel(
            uiMessagePresenter = mockedUiMessagePresenter,
            navigation = mockedNavigator,
            stringResourceProvider = mockedStringResourceProvider,
            backOfficeNetwork = mockedBackOfficeNetwork
        )
    }

    @Test
    fun `while requesting articles, shows progress indicator `() {
        coEvery { mockedBackOfficeNetwork.getArticles() } returns ApiCallResult.Failure(Throwable())

        testCoroutineDispatcher.pauseDispatcher()
        subject.onInteraction(ScreenEntered)
        assertEquals(true, subject.isProgressIndicatorVisibleLiveData.value)

        testCoroutineDispatcher.resumeDispatcher()
        assertEquals(false, subject.isProgressIndicatorVisibleLiveData.value)
    }

    @Test
    fun `when screen entered, requests articles`() {
        subject.onInteraction(ScreenEntered)

        coVerify { mockedBackOfficeNetwork.getArticles() }
    }

    @Test
    fun `when request for articles is canceled, shows toast message`() {
        coEvery { mockedBackOfficeNetwork.getArticles() } returns ApiCallResult.Cancelled(Throwable("Sample Error"))
        coEvery { mockedStringResourceProvider.getString(R.string.api_request_cancelled) } returns "Request cancelled."

        subject.onInteraction(ScreenEntered)

        verify { mockedUiMessagePresenter.showToastMessage("Request cancelled.") }
    }

    @Test
    fun `when request for articles fails, shows toast message`() {
        coEvery { mockedBackOfficeNetwork.getArticles() } returns ApiCallResult.Failure(Throwable("Sample Error"))
        coEvery { mockedStringResourceProvider.getString(R.string.api_request_failure) } returns "Failed to fetch articles."

        subject.onInteraction(ScreenEntered)

        verify { mockedUiMessagePresenter.showToastMessage("Failed to fetch articles.") }
    }

    @Test
    fun `when request for articles is successful, render articles`() {
        val sampleArticlesRaw = ArticlesListWrapper(listOf(ArticleApiModel(1, "1", "1", "1", "1")))
        val sampleArticlesUi = listOf(ArticleItem.Article(1, "1", "1"))

        coEvery { mockedBackOfficeNetwork.getArticles() } returns ApiCallResult.Success(sampleArticlesRaw)

        subject.onInteraction(ScreenEntered)

        assertEquals(sampleArticlesUi, subject.articlesLiveData.value)
    }

    @Test
    fun `navigates to next screen when continue button is clicked`() {
        subject.onInteraction(OnArticleItemClicked(1))

        verify { mockedNavigator.navigateToArticleDetailsScreen(1) }
    }
}
