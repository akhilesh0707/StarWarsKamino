package com.starwars.kamino.ui.residents.repository

import com.starwars.kamino.ui.residents.api.ResidentService
import com.starwars.kamino.ui.residents.model.ResidentModel
import io.reactivex.Observable
import javax.inject.Inject

class DefaultResidentRepository @Inject constructor(private val service: ResidentService) : ResidentRepository {

    override fun getResident(residentId: Int): Observable<ResidentModel> {
        return service.getResident(residentId)
    }
}