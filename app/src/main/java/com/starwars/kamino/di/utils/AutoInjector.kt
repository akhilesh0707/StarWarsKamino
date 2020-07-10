package com.starwars.kamino.di.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector

interface Injectable

fun Application.applyAutoInjector() = registerActivityLifecycleCallbacks(
    object : Application.ActivityLifecycleCallbacks {
        override fun onActivityPaused(activity: Activity?) {
        }

        override fun onActivityResumed(activity: Activity?) {
        }

        override fun onActivityStarted(activity: Activity?) {
        }

        override fun onActivityDestroyed(activity: Activity?) {
        }

        override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
        }

        override fun onActivityStopped(activity: Activity?) {
        }

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            handleActivity(activity)
        }

    }
)

private fun handleActivity(activity: Activity) {

    if (activity is Injectable || activity is HasSupportFragmentInjector) {
        AndroidInjection.inject(activity)
    }

    (activity as? FragmentActivity)?.supportFragmentManager?.registerFragmentLifecycleCallbacks(
        object : FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentCreated(
                fragmentManager: FragmentManager,
                fragment: Fragment,
                savedInstanceState: Bundle?
            ) {
                super.onFragmentCreated(fragmentManager, fragment, savedInstanceState)
                if (fragment is Injectable) {
                    AndroidSupportInjection.inject(fragment)
                }
            }
        }, true
    )
}