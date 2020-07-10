package com.starwars.kamino.ui.planet.repository

import com.starwars.kamino.ui.planet.api.PlanetService
import com.starwars.kamino.ui.planet.model.PlanetModel
import io.reactivex.Single
import javax.inject.Inject

class DefaultPlanetRepository @Inject constructor(private val service: PlanetService) :
    PlanetRepository {

    override fun getPlanet(planetId: Int): Single<PlanetModel> {
        return service.getPlanet(planetId)
    }
}