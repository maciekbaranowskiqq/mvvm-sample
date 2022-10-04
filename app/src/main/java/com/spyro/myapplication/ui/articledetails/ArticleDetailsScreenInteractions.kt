package com.spyro.myapplication.ui.articledetails

sealed class ArticleDetailsScreenInteractions {
    data class ScreenEntered(val id: Long) : ArticleDetailsScreenInteractions()
}
