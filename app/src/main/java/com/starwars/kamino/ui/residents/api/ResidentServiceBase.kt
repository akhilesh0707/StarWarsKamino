package com.starwars.kamino.ui.residents.api

import com.starwars.kamino.ui.residents.model.ResidentModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface ResidentServiceBase {

    /**
     * Get individual resident detail using resident id from API
     * @param id
     */
    @GET("residents/{id}")
    fun fetchResidentApi(@Path("id") id: Int): Observable<ResidentModel>
}