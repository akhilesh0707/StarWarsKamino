package com.starwars.kamino.ui.planet

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.RequestManager
import com.starwars.kamino.R
import com.starwars.kamino.base.BaseActivity
import com.starwars.kamino.ui.planet.model.PlanetModel
import com.starwars.kamino.utils.bindViewModel
import com.starwars.kamino.utils.makeGone
import com.starwars.kamino.utils.makeVisible
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
                progress.makeVisible()
            }
            is PlanetUIModel.Success -> {
                progress.makeGone()
                // Set planet header information
                bindPlanetHeader(uiModel.planetModel)
                // Set planet all the detail
                bindPlanetDetail(uiModel.planetModel)
                Timber.d(uiModel.planetModel.toString())
            }
            is PlanetUIModel.Error -> {
                progress.makeGone()
                Timber.e(uiModel.error)
            }
        }
    }

    /**
     * Set Planet name and planet image on top section
     * @param planetModel
     */
    private fun bindPlanetHeader(planetModel: PlanetModel) {
        textPlanetName.text = planetModel.name
        requestManager.load(planetModel.imageUrl).into(imagePlanet)
    }

    /**
     * Set Planet all detail (Population, Climate, Rotation period, Orbital period, Gravity, Surface water, Terrain)
     * @param planetModel
     * */
    private fun bindPlanetDetail(planetModel: PlanetModel) {
        textPopulation.text = getString(R.string.text_population, planetModel.population.toString())
        textClimate.text = getString(R.string.text_climate, planetModel.climate)
        textRotationPeriod.text =
            getString(R.string.text_rotation_period, planetModel.rotationPeriod.toString())
        textOrbitalPeriod.text =
            getString(R.string.text_orbital_period, planetModel.orbitalPeriod.toString())
        textGravity.text = getString(R.string.text_gravity, planetModel.gravity)
        textSurfaceWater.text =
            getString(R.string.text_surface_water, planetModel.surfaceWater.toString())
        textTerrain.text = getString(R.string.text_terrain, planetModel.terrain)
    }
}
