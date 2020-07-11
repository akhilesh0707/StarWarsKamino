package com.starwars.kamino.ui.residents

import androidx.lifecycle.LiveData
import com.starwars.kamino.base.BaseViewModel
import com.starwars.kamino.ui.planet.model.PlanetModel
import com.starwars.kamino.ui.residents.model.ResidentModel
import com.starwars.kamino.ui.residents.repository.ResidentRepository
import com.starwars.kamino.utils.TransientAwareConsumerLiveData
import com.starwars.kamino.utils.TransientAwareUiModel
import com.starwars.kamino.utils.addTo
import com.starwars.kamino.utils.runOnBackground
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject

sealed class ResidentUIModel : TransientAwareUiModel() {
    object Loading : ResidentUIModel()
    data class Error(var error: String = "") : ResidentUIModel()
    data class Success(val residentModel: ResidentModel) : ResidentUIModel()
}

class ResidentsViewModel @Inject constructor(private val repository: ResidentRepository) :
    BaseViewModel() {

    var planet: PlanetModel? = null
    val residentList: ArrayList<ResidentModel> = arrayListOf()
    private val _residentUIModel = TransientAwareConsumerLiveData<ResidentUIModel>()
    var residentUIModel: LiveData<ResidentUIModel> = _residentUIModel

    /**
     * Get resident from Resident repository [repository]
     */
    fun getResident() {
        planet?.let {
            Observable
                .fromIterable(it.residents)
                .concatMap {
                    val residentId = getResidentId(it)
                    repository.getResident(residentId)
                }
                .runOnBackground(schedulerProvider)
                .map { ResidentUIModel.Success(it) as ResidentUIModel }
                .startWith(ResidentUIModel.Loading)
                .doOnError { Timber.d(it) }
                .onErrorReturn { ResidentUIModel.Error(it.message ?: "") }
                .subscribe(_residentUIModel)
                .addTo(disposable)
        }
    }

    /**
     * Get resident id from resident url
     * @param residentUrl
     */
    private fun getResidentId(residentUrl: String) =
        residentUrl.trim().substring(residentUrl.lastIndexOf("/")).replace("/", "").toInt()
}