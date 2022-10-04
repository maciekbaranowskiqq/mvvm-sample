package com.spyro.myapplication.ui.articleslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spyro.myapplication.R
import com.spyro.myapplication.utility.StringResourceProvider
import com.spyro.myapplication.utility.UiMessagePresenter
import com.spyro.network.api.BackOfficeNetwork
import com.spyro.network.api.model.ArticleApiModel
import com.spyro.network.common.ApiCallResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticlesScreenViewModel @Inject constructor(
    private val navigation: ArticlesScreenNavigator,
    private val backOfficeNetwork: BackOfficeNetwork,
    private val uiMessagePresenter: UiMessagePresenter,
    private val stringResourceProvider: StringResourceProvider
) : ViewModel() {

    private val _isProgressIndicatorVisibleLiveData = MutableLiveData<Boolean>().apply { value = false }
    val isProgressIndicatorVisibleLiveData: LiveData<Boolean> = _isProgressIndicatorVisibleLiveData

    private val _articlesLiveData = MutableLiveData<List<ArticleItem.Article>>()
    val articlesLiveData: LiveData<List<ArticleItem.Article>> = _articlesLiveData

    fun onInteraction(interaction: ArticlesScreenInteractions) {
        when (interaction) {
            is ArticlesScreenInteractions.ScreenEntered -> onScreenEntered()
            is ArticlesScreenInteractions.OnArticleItemClicked -> onArticleItemClicked(interaction.id)
        }
    }

    private fun onArticleItemClicked(id: Long) {
        navigation.navigateToArticleDetailsScreen(id)
    }

    private fun onScreenEntered() {
        showProgressIndicator()
        viewModelScope.launch {
            when (val result = backOfficeNetwork.getArticles()) {
                is ApiCallResult.Cancelled -> uiMessagePresenter.showToastMessage(stringResourceProvider.getString(R.string.api_request_cancelled))
                is ApiCallResult.Failure -> uiMessagePresenter.showToastMessage(stringResourceProvider.getString(R.string.api_request_failure))
                is ApiCallResult.Success -> onArticlesFetched(result.body.articles.map { it.toArticleItem() })
            }
            hideProgressIndicator()
        }
    }

    private fun onArticlesFetched(articles: List<ArticleItem.Article>) {
        _articlesLiveData.value = articles
    }

    private fun showProgressIndicator() {
        _isProgressIndicatorVisibleLiveData.value = true
    }

    private fun hideProgressIndicator() {
        _isProgressIndicatorVisibleLiveData.value = false
    }

    private fun ArticleApiModel.toArticleItem(): ArticleItem.Article = ArticleItem.Article(
        id = id,
        title = title,
        subtitle = subtitle,
    )
}
