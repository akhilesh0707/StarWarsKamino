package com.starwars.kamino.ui.residents.api

import com.starwars.kamino.di.scopes.ApplicationScope
import com.starwars.kamino.ui.residents.model.ResidentModel
import io.reactivex.Observable
import javax.inject.Inject

@ApplicationScope
class DefaultResidentService @Inject constructor(private val service: ResidentServiceBase) :
    ResidentService {

    /**
     * Get individual resident detail using resident id
     * @param residentId
     */
    override fun getResident(residentId: Int): Observable<ResidentModel> {
        return service.fetchResidentApi(residentId)
    }
}