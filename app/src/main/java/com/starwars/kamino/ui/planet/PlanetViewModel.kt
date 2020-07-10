package com.starwars.kamino.ui.planet

import androidx.lifecycle.LiveData
import com.starwars.kamino.base.DisposableViewModel
import com.starwars.kamino.di.utils.TransientAwareConsumerLiveData
import com.starwars.kamino.di.utils.TransientAwareUiModel
import com.starwars.kamino.ui.planet.model.PlanetModel
import com.starwars.kamino.ui.planet.repository.PlanetRepository
import javax.inject.Inject

sealed class PlanetUIModel : TransientAwareUiModel() {
    object Loading : PlanetUIModel()
    data class Error(var error: String = "") : PlanetUIModel()
    data class Success(val planetModel: PlanetModel) : PlanetUIModel()
}

class PlanetViewModel @Inject constructor(private val repository: PlanetRepository) :
    DisposableViewModel() {
    private val _planetUIModel = TransientAwareConsumerLiveData<PlanetUIModel>()
    var planetUIModel: LiveData<PlanetUIModel> = _planetUIModel

    companion object {
        private const val KAMINO_PLANET_ID = 10
    }

    fun getPlanet() {
        repository.getPlanet(KAMINO_PLANET_ID)

    }
}