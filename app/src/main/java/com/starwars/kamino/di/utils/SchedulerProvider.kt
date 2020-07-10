package com.starwars.kamino.di.utils

import io.reactivex.Scheduler

interface SchedulerProvider {
    fun io(): Scheduler
    fun computation(): Scheduler
    fun mainThread(): Scheduler

    companion object {
        fun getInstance(): SchedulerProvider = AppSchedulerProvider.instance
    }
}