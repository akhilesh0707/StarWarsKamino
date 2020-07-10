package com.starwars.kamino.di.modules

import com.starwars.kamino.ui.planet.api.DefaultPlanetService
import com.starwars.kamino.ui.planet.api.PlanetService
import dagger.Binds
import dagger.Module

@Module
abstract class ServiceModule {

    @Binds
    abstract fun bindPlanetService(service: DefaultPlanetService): PlanetService

}