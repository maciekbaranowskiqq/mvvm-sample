package com.spyro.myapplication.ui.articleslist

sealed class ArticlesScreenInteractions {
    object ScreenEntered : ArticlesScreenInteractions()
    data class OnArticleItemClicked(val id: Long) : ArticlesScreenInteractions()
}
