package com.starwars.kamino.ui.planet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.RequestManager
import com.starwars.kamino.R
import com.starwars.kamino.base.BaseFragment
import com.starwars.kamino.ui.planet.model.LikeModel
import com.starwars.kamino.ui.planet.model.PlanetModel
import com.starwars.kamino.utils.bindViewModel
import com.starwars.kamino.utils.makeGone
import com.starwars.kamino.utils.makeVisible
import com.starwars.kamino.utils.zoomImageFromThumb
import kotlinx.android.synthetic.main.fragment_planet.*
import timber.log.Timber
import javax.inject.Inject

class PlanetFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var requestManager: RequestManager
    private val viewModel by bindViewModel<PlanetViewModel>(lazy { viewModelFactory })
    private var planetModel: PlanetModel?=null

    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.
    private var shortAnimationDuration: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_planet, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Call get plant api
        viewModel.apply {
            getPlanet()
            planetUIModel.observe(viewLifecycleOwner, Observer {
                onUiModelChanged(it)
            })
        }
        // Bind click listeners
        bindClickListeners()

        // Retrieve and cache the system's default "short" animation time.
        shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.planetUIModel.removeObservers(this)
    }

    /**
     * Bind all click listeners
     */
    private fun bindClickListeners() {
        // Bind click on residents button
        buttonResidents.setOnClickListener {view ->
            planetModel?.let{
                navigateToResidents(view, it)
            }
        }

        // Bind clicks on the thumbnail views.
        imagePlanet.setOnClickListener {
            (it as ImageView).zoomImageFromThumb(shortAnimationDuration, root, imageExpand)
        }

        // Bind like click
        imageLike.setOnClickListener {
            viewModel.likePlanet()
        }
    }

    /**
     * Handle live data changes (start progress, get result and API error)
     * @param uiModel : LiveData result
     */
    private fun onUiModelChanged(uiModel: PlanetUIModel) {
        if (uiModel.isRedelivered)
            return

        when (uiModel) {
            is PlanetUIModel.Loading -> {
                progressBar.makeVisible()
            }
            is PlanetUIModel.Success -> {
                progressBar.makeGone()
                uiModel.planetModel.let {
                    planetModel = it
                    // Set planet header information
                    setPlanetHeader(it)
                    // Set planet all the detail
                    setPlanetDetail(it)
                    Timber.d(it.toString())
                }
            }
            is PlanetUIModel.SuccessLike -> {
                progressBar.makeGone()
                setLikeCount(uiModel.likeModel)
            }
            is PlanetUIModel.Error -> {
                progressBar.makeGone()
                Toast.makeText(this.context, uiModel.error, Toast.LENGTH_LONG).show()
                Timber.e(uiModel.error)
            }
        }
    }

    /**
     * Set like count and disabled like button click
     * @param likeModel : Like count
     */
    private fun setLikeCount(likeModel: LikeModel) {
        textLikeCount.text = likeModel.likes.toString()
        imageLike.apply {
            isClickable = false
            setImageResource(R.drawable.ic_like)
        }
    }

    /**
     * Set Planet name and planet image on top section
     * @param planetModel : Planet detail
     */
    private fun setPlanetHeader(planetModel: PlanetModel) {
        textPlanetName.text = planetModel.name
        requestManager.load(planetModel.imageUrl).into(imagePlanet)
    }

    /**
     * Set Planet all detail (Population, Climate, Rotation period, Orbital period, Gravity, Surface water, Terrain)
     * @param planetModel : Planet detail
     * */
    private fun setPlanetDetail(planetModel: PlanetModel) {
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

    /**
     * Navigate to Residents Screen
     * @param view : View to find navigation controller
     * @param planetModel : Planet detail with list of residents
     */
    private fun navigateToResidents(view: View, planetModel: PlanetModel) {
        // Navigate to the ResidentsFragment using navController and safeArgs
        val action = PlanetFragmentDirections.actionFragmentPlanetToResident(planetModel)
        view.findNavController().navigate(action)
    }

}