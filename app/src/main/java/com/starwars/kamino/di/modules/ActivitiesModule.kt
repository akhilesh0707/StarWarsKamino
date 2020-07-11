package com.starwars.kamino.di.modules

import com.starwars.kamino.ui.HostActivity
import com.starwars.kamino.di.scopes.ActivityScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivitiesModule {

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): HostActivity
}