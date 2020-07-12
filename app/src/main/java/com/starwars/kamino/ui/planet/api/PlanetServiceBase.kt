package com.starwars.kamino.ui.planet.api

import com.starwars.kamino.ui.planet.model.LikeModel
import com.starwars.kamino.ui.planet.model.PlanetModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PlanetServiceBase {

    /**
     * Get planet detail api
     * @param id
     */
    @GET("planets/{id}")
    fun fetchPlanetApi(@Path("id") id: Int): Single<PlanetModel>

    /**
     * Like planet and get like count
     * @param id
     */
    @POST("planets/{id}/like")
    fun likePlanetApi(@Path("id") id: Int): Single<LikeModel>

}