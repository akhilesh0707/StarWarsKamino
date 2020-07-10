package com.starwars.kamino.ui.planet

import androidx.lifecycle.LiveData
import com.starwars.kamino.base.BaseViewModel
import com.starwars.kamino.di.utils.TransientAwareConsumerLiveData
import com.starwars.kamino.di.utils.TransientAwareUiModel
import com.starwars.kamino.di.utils.addTo
import com.starwars.kamino.di.utils.runOnBackground
import com.starwars.kamino.ui.planet.model.PlanetModel
import com.starwars.kamino.ui.planet.repository.PlanetRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

sealed class PlanetUIModel : TransientAwareUiModel() {
    object Loading : PlanetUIModel()
    data class Error(var error: String = "") : PlanetUIModel()
    data class Success(val planetModel: PlanetModel) : PlanetUIModel()
}

class PlanetViewModel @Inject constructor(private val repository: PlanetRepository) :
    BaseViewModel() {

    private val _planetUIModel = TransientAwareConsumerLiveData<PlanetUIModel>()
    var planetUIModel: LiveData<PlanetUIModel> = _planetUIModel

    companion object {
        private const val KAMINO_PLANET_ID = 10
    }

    fun getPlanet() {
        repository.getPlanet(KAMINO_PLANET_ID)
            .runOnBackground(schedulerProvider)
            .toObservable()
            .map { PlanetUIModel.Success(it) as PlanetUIModel }
            .startWith(PlanetUIModel.Loading)
            .doOnError { Timber.d(it) }
            .onErrorReturn { PlanetUIModel.Error(it.message ?: "") }
            .subscribe(_planetUIModel)
            .addTo(disposable)
    }
}