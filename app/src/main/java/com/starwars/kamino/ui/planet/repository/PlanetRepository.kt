package com.starwars.kamino.ui.planet.repository

import com.starwars.kamino.base.Repository
import com.starwars.kamino.ui.planet.model.LikeModel
import com.starwars.kamino.ui.planet.model.PlanetModel
import io.reactivex.Single

interface PlanetRepository : Repository {
    /**
     * Get Planet from API
     * @param planetId
     */
    fun getPlanet(planetId: Int): Single<PlanetModel>

    /**
     * Like planet and get like count API
     * @param planetId
     */
    fun likPlanet(planetId: Int): Single<LikeModel>
}