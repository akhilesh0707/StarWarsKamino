package com.starwars.kamino.ui.planet.api

import com.starwars.kamino.di.utils.RetrofitServiceWrapper
import com.starwars.kamino.ui.planet.model.PlanetModel
import io.reactivex.Single

interface PlanetService : RetrofitServiceWrapper {
    fun getPlanet(planetId: Int): Single<PlanetModel>
}