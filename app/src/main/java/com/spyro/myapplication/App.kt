package com.spyro.myapplication

import android.app.Application
import com.spyro.myapplication.utility.MainActivityProvider
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var mainActivityProvider: MainActivityProvider

    override fun onCreate() {
        super.onCreate()

        mainActivityProvider.register(this)
    }
}
