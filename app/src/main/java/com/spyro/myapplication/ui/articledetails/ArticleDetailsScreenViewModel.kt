package com.spyro.myapplication.ui.articledetails

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
class ArticleDetailsScreenViewModel @Inject constructor(
    private val backOfficeNetwork: BackOfficeNetwork,
    private val uiMessagePresenter: UiMessagePresenter,
    private val stringResourceProvider: StringResourceProvider,
) : ViewModel() {

    private val _isProgressIndicatorVisibleLiveData = MutableLiveData<Boolean>().apply { value = false }
    val isProgressIndicatorVisibleLiveData: LiveData<Boolean> = _isProgressIndicatorVisibleLiveData

    private val _articleDetailsLiveData = MutableLiveData<ArticleDetailsUiModel>()
    val articleDetailsLiveData: LiveData<ArticleDetailsUiModel> = _articleDetailsLiveData

    fun onInteraction(interaction: ArticleDetailsScreenInteractions) {
        when (interaction) {
            is ArticleDetailsScreenInteractions.ScreenEntered -> onScreenEntered(interaction.id)
        }
    }

    private fun onScreenEntered(id: Long) {
        showProgressIndicator()
        viewModelScope.launch {
            when (val result = backOfficeNetwork.getArticleDetails(id)) {
                is ApiCallResult.Cancelled -> uiMessagePresenter.showToastMessage(stringResourceProvider.getString(R.string.api_request_cancelled))
                is ApiCallResult.Failure -> uiMessagePresenter.showToastMessage(stringResourceProvider.getString(R.string.api_request_failure))
                is ApiCallResult.Success -> onArticleDetailsFetched(result.body.article.toArticleDetailsUiModel())
            }
            hideProgressIndicator()
        }
    }

    private fun onArticleDetailsFetched(body: ArticleDetailsUiModel) {
        _articleDetailsLiveData.value = body
    }

    private fun showProgressIndicator() {
        _isProgressIndicatorVisibleLiveData.value = true
    }

    private fun hideProgressIndicator() {
        _isProgressIndicatorVisibleLiveData.value = false
    }

    data class ArticleDetailsUiModel(
        val id: Long,
        val title: String,
        val subtitle: String,
        val body: String,
        val date: String,
    )

    private fun ArticleApiModel.toArticleDetailsUiModel(): ArticleDetailsUiModel = ArticleDetailsUiModel(
        id = id,
        title = title,
        subtitle = subtitle,
        body = body ?: "Missing content.",
        date = date ?: "Missing date.",
    )
}
