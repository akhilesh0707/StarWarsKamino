package com.starwars.kamino.ui.planet.api

import com.starwars.kamino.di.utils.RetrofitServiceWrapper
import com.starwars.kamino.ui.planet.model.LikeModel
import com.starwars.kamino.ui.planet.model.PlanetModel
import io.reactivex.Single

interface PlanetService : RetrofitServiceWrapper {
    /**
     * Get Planet detail
     * @param planetId
     */
    fun getPlanet(planetId: Int): Single<PlanetModel>

    /**
     * Like planet and get planet like count
     * @param planetId
     */
    fun likePlanet(planetId: Int): Single<LikeModel>
}