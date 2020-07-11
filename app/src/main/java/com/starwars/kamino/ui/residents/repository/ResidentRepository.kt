package com.starwars.kamino.ui.residents.repository

import com.starwars.kamino.base.Repository
import com.starwars.kamino.ui.planet.model.PlanetModel
import com.starwars.kamino.ui.residents.model.ResidentModel
import io.reactivex.Observable
import io.reactivex.Single

interface ResidentRepository : Repository {
    /**
     * Get Resident from API
     * @param residentId
     */
    fun getResident(residentId: Int): Observable<ResidentModel>
}