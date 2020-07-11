package com.starwars.kamino.di.modules

import com.starwars.kamino.di.scopes.FragmentScope
import com.starwars.kamino.ui.planet.PlanetFragment
import com.starwars.kamino.ui.residents.ResidentsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentsModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributePlanetFragment(): PlanetFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeResidentsFragment(): ResidentsFragment
}