package com.starwars.kamino.ui.residents

import com.starwars.kamino.base.BaseViewModel
import com.starwars.kamino.ui.planet.repository.PlanetRepository
import javax.inject.Inject

class ResidentsViewModel @Inject constructor(private val repository: PlanetRepository) :
    BaseViewModel() {

}