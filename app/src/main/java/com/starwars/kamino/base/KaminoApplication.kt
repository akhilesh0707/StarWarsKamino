package com.starwars.kamino.base

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment
import com.starwars.kamino.di.utils.applyAutoInjector
import com.starwars.kamino.di.components.ApplicationComponent
import com.starwars.kamino.di.components.DaggerApplicationComponent
import com.starwars.kamino.di.modules.ApplicationModule
import com.starwars.kamino.di.modules.NetworkModule
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

open class KaminoApplication : Application(), HasActivityInjector, HasSupportFragmentInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>
    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    lateinit var applicationComponent: ApplicationComponent

    override fun activityInjector(): AndroidInjector<Activity> {
        return activityInjector
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }

    override fun onCreate() {
        super.onCreate()

        applicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .networkModule(NetworkModule())
            .build()
        applicationComponent.inject(this)

        applyAutoInjector()
    }

}