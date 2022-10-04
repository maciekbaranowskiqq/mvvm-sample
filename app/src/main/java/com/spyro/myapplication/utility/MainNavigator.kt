package com.spyro.myapplication.utility

import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.spyro.myapplication.R
import javax.inject.Inject

open class MainNavigator @Inject constructor(
    private val mainActivityProvider: MainActivityProvider,
) {
    protected val navController: NavController?
        get() = mainActivityProvider.getMainActivity()?.findNavController(R.id.fragmentContainerView)
}
