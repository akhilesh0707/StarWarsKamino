package com.starwars.kamino.di.modules

import com.starwars.kamino.ui.planet.repository.DefaultPlanetRepository
import com.starwars.kamino.ui.planet.repository.PlanetRepository
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindPlanetRepository(repository: DefaultPlanetRepository): PlanetRepository

}