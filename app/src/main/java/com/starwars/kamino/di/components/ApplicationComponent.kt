package com.starwars.kamino.di.components

import com.starwars.kamino.base.KaminoApplication
import com.starwars.kamino.di.modules.*
import com.starwars.kamino.di.scopes.ApplicationScope
import dagger.Component
import dagger.android.AndroidInjectionModule

@ApplicationScope
@Component(
    modules = [
        AndroidInjectionModule::class,
        ApplicationModule::class,
        ActivitiesModule::class,
        NetworkModule::class,
        ViewModelModule::class,
        RepositoryModule::class,
        ServiceModule::class,
        SystemServiceModule::class
    ]
)

interface ApplicationComponent {
    fun inject(application: KaminoApplication)
}
