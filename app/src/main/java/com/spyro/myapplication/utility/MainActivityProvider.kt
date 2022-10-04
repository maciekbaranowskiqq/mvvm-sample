package com.spyro.myapplication.utility

import android.app.Activity
import android.app.Application
import com.spyro.myapplication.activity.MainActivity
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainActivityProvider @Inject constructor() {

    private var mainActivity: WeakReference<MainActivity>? = null

    private val activityLifecycleCallbacks = object : ActivityLifecycleAdapter() {
        override fun onActivityStarted(activity: Activity) {
            if (activity is MainActivity) {
                mainActivity = WeakReference(activity)
            }
        }

        override fun onActivityStopped(activity: Activity) {
            if (activity is MainActivity) {
                mainActivity = null
            }
        }
    }

    fun getMainActivity(): MainActivity? {
        return mainActivity?.get()
    }

    fun register(application: Application) {
        application.registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
    }
}
