package com.starwars.kamino.ui.planet.api

import com.starwars.kamino.ui.planet.model.PlanetModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface PlanetServiceBase {

    @GET("planets/{id}")
    fun fetchPlanetApi(@Path("id") id: Int): Single<PlanetModel>
}