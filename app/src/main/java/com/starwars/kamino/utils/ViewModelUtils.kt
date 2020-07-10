package com.starwars.kamino.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

/**
 * Get a ViewModel for the [Activity]
 */
inline fun <reified T : ViewModel> FragmentActivity.bindViewModel(lazyViewModelProviderFactory: Lazy<ViewModelProvider.Factory>): Lazy<T> {
    return lazy {
        ViewModelProviders.of(this, lazyViewModelProviderFactory.value).get(T::class.java)
    }
}

/**
 * Get a ViewModel for the  [Fragment]
 */
inline fun <reified T : ViewModel> Fragment.bindViewModel(lazyViewModelProviderFactory: Lazy<ViewModelProvider.Factory>): Lazy<T> {
    return lazy {
        ViewModelProviders.of(this, lazyViewModelProviderFactory.value).get(T::class.java)
    }
}

