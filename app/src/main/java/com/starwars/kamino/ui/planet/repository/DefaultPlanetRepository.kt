package com.starwars.kamino.ui.planet.repository

import com.starwars.kamino.ui.planet.api.PlanetService
import com.starwars.kamino.ui.planet.model.LikeModel
import com.starwars.kamino.ui.planet.model.PlanetModel
import io.reactivex.Single
import javax.inject.Inject

class DefaultPlanetRepository @Inject constructor(private val service: PlanetService) : PlanetRepository {

    /**
     * Get the planet detail API
     * @param planetId
     */
    override fun getPlanet(planetId: Int): Single<PlanetModel> {
        return service.getPlanet(planetId)
    }

    /**
     * Like planet and get like count API
     * @param planetId
     */
    override fun likPlanet(planetId: Int): Single<LikeModel> {
       return service.likePlanet(planetId)
    }
}