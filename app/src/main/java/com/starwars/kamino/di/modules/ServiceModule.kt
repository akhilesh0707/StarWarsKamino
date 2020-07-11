package com.starwars.kamino.di.modules

import com.starwars.kamino.ui.planet.api.DefaultPlanetService
import com.starwars.kamino.ui.planet.api.PlanetService
import com.starwars.kamino.ui.residents.api.DefaultResidentService
import com.starwars.kamino.ui.residents.api.ResidentService
import dagger.Binds
import dagger.Module

@Module
abstract class ServiceModule {

    @Binds
    abstract fun bindPlanetService(service: DefaultPlanetService): PlanetService

    @Binds
    abstract fun bindResidentService(service: DefaultResidentService): ResidentService

}