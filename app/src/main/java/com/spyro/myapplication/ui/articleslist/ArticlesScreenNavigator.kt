package com.spyro.myapplication.ui.articleslist

import com.spyro.myapplication.utility.MainActivityProvider
import com.spyro.myapplication.utility.MainNavigator
import javax.inject.Inject

class ArticlesScreenNavigator @Inject constructor(
    mainActivityProvider: MainActivityProvider
) : MainNavigator(mainActivityProvider) {

    fun navigateToArticleDetailsScreen(id: Long) {
        navController?.navigate(ArticlesScreenFragmentDirections.actionToArticleDetailsScreen(id))
    }
}
