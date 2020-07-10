package com.starwars.kamino.di.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import io.reactivex.functions.Consumer


open class ConsumerLiveData<T> : LiveData<T>(), Consumer<T> {
    override fun accept(t: T) = postValue(t)

    fun acceptAsSet(t: T) {
        value = t
    }
}


class TransientAwareConsumerLiveData<T : TransientAwareUiModel> : ConsumerLiveData<T>() {

    private var previousValue: T? = null

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, Observer { value: T? ->
            value?.isRedelivered = false
            val inProperState =
                owner.lifecycle.currentState == Lifecycle.State.CREATED || owner.lifecycle.currentState == Lifecycle.State.STARTED
            if (previousValue == value && inProperState) {
                value?.isRedelivered = true
            }

            observer.onChanged(value)
            previousValue = value
        })
    }
}