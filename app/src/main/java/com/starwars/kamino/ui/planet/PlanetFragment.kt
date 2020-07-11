package com.starwars.kamino.ui.planet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.RequestManager
import com.starwars.kamino.R
import com.starwars.kamino.base.BaseFragment
import com.starwars.kamino.ui.planet.model.PlanetModel
import com.starwars.kamino.utils.bindViewModel
import com.starwars.kamino.utils.makeGone
import com.starwars.kamino.utils.makeVisible
import kotlinx.android.synthetic.main.fragment_planet.*
import timber.log.Timber
import javax.inject.Inject

class PlanetFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var requestManager: RequestManager
    private lateinit var planetModel: PlanetModel
    private val viewModel by bindViewModel<PlanetViewModel>(lazy { viewModelFactory })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_planet, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getPlanet()
        viewModel.planetUIModel.observe(viewLifecycleOwner, Observer {
            onUiModelChanged(it)
        })
        buttonResidents.setOnClickListener {
            navigateToResidents(it, planetModel)
        }
    }

    /**
     * Handle live data changes (start progress, get result and API error)
     * @param uiModel
     */
    private fun onUiModelChanged(uiModel: PlanetUIModel) {
        when (uiModel) {
            is PlanetUIModel.Loading -> {
                progressBar.makeVisible()
            }
            is PlanetUIModel.Success -> {
                progressBar.makeGone()
                uiModel.planetModel.let {
                    planetModel = it
                    // Set planet header information
                    bindPlanetHeader(it)
                    // Set planet all the detail
                    bindPlanetDetail(it)
                    Timber.d(it.toString())
                }

            }
            is PlanetUIModel.Error -> {
                progressBar.makeGone()
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

    private fun navigateToResidents(view: View, planetModel: PlanetModel) {
        // Navigate to the ResidentListFragment using navController and safeArgs
        val action = PlanetFragmentDirections.directionResidents(planetModel)
        view.findNavController().navigate(action)
    }
}