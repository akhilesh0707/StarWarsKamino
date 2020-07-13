package com.starwars.kamino.ui.residents.api

import com.starwars.kamino.di.utils.RetrofitServiceWrapper
import com.starwars.kamino.ui.residents.model.ResidentModel
import io.reactivex.Observable

interface ResidentService : RetrofitServiceWrapper {
    /**
     * Get individual resident detail using resident id
     * @param residentId : Resident id
     */
    fun getResident(residentId: Int): Observable<ResidentModel>
}