package com.starwars.kamino.ui.planet.api

import com.starwars.kamino.di.scopes.ApplicationScope
import com.starwars.kamino.ui.planet.model.LikeModel
import com.starwars.kamino.ui.planet.model.PlanetModel
import io.reactivex.Single
import javax.inject.Inject

@ApplicationScope
class DefaultPlanetService @Inject constructor(private val service: PlanetServiceBase) : PlanetService {
    /**
     * Get planet detail api
     * @param planetId
     */
    override fun getPlanet(planetId: Int): Single<PlanetModel> {
        return service.fetchPlanetApi(planetId)
    }

    /**
     * Like planet and get like count
     * @param planetId
     */
    override fun likePlanet(planetId: Int): Single<LikeModel> {
        return service.likePlanetApi(planetId)
    }
}