package com.starwars.kamino.ui.planet.repository

import com.starwars.kamino.base.Repository
import com.starwars.kamino.ui.planet.model.PlanetModel
import io.reactivex.Single

interface PlanetRepository : Repository {
    fun getPlanet(planetId: Int): Single<PlanetModel>
}