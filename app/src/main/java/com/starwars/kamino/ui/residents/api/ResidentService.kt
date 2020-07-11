package com.starwars.kamino.ui.residents.api

import com.starwars.kamino.di.utils.RetrofitServiceWrapper
import com.starwars.kamino.ui.planet.model.PlanetModel
import com.starwars.kamino.ui.residents.model.ResidentModel
import io.reactivex.Observable
import io.reactivex.Single

interface ResidentService : RetrofitServiceWrapper {
    fun getResident(residentId: Int): Observable<ResidentModel>
}