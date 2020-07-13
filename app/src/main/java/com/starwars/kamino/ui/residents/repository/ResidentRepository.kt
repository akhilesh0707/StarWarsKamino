package com.starwars.kamino.ui.residents.repository

import com.starwars.kamino.base.Repository
import com.starwars.kamino.ui.residents.model.ResidentModel
import io.reactivex.Observable

interface ResidentRepository : Repository {
    /**
     * Get Resident from API
     * @param residentId : Resident id
     */
    fun getResident(residentId: Int): Observable<ResidentModel>
}