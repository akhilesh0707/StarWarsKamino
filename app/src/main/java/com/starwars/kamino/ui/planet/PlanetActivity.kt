package com.starwars.kamino.ui.planet

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.RequestManager
import com.starwars.kamino.R
import com.starwars.kamino.base.BaseActivity
import com.starwars.kamino.ui.planet.model.PlanetModel
import com.starwars.kamino.utils.bindViewModel
import kotlinx.android.synthetic.main.activity_planet.*
import timber.log.Timber
import javax.inject.Inject

class PlanetActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var requestManager: RequestManager
    private val viewModel by bindViewModel<PlanetViewModel>(lazy { viewModelFactory })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planet)
        viewModel.getPlanet()
        viewModel.planetUIModel.observe(this, Observer {
            onUiModelChanged(it)
        })
    }

    /**
     * Handle live data changes (start progress, get result and API error)
     * @param uiModel
     */
    private fun onUiModelChanged(uiModel: PlanetUIModel) {
        when (uiModel) {
            is PlanetUIModel.Loading -> {
            }
            is PlanetUIModel.Success -> {
                bindHeader(uiModel.planetModel)
                Timber.d(uiModel.planetModel.toString())
            }
            is PlanetUIModel.Error -> {
                Timber.e(uiModel.error)
            }
        }
    }

    /**
     * Set Planet name and planet image on top section
     * @param planetModel
     */
    private fun bindHeader(planetModel: PlanetModel) {
        textPlanetName.text = planetModel.name
        requestManager.load(planetModel.imageUrl).into(imagePlanet)
    }
}
