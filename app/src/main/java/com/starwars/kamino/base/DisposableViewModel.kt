package com.starwars.kamino.base

import androidx.lifecycle.ViewModel
import com.starwars.kamino.di.utils.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

abstract class DisposableViewModel : ViewModel() {
    protected val disposable = CompositeDisposable()
    protected val schedulerProvider = SchedulerProvider.getInstance()

    override fun onCleared() {
        disposable.clear()
    }
}
