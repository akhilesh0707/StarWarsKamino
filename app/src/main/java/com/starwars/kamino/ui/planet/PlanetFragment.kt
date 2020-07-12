package com.starwars.kamino.ui.planet

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
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
import kotlinx.android.synthetic.main.fragment_planet.*
import timber.log.Timber
import javax.inject.Inject

class PlanetFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var requestManager: RequestManager
    private val viewModel by bindViewModel<PlanetViewModel>(lazy { viewModelFactory })
    private lateinit var planetModel: PlanetModel

    // Hold a reference to the current animator,
    // so that it can be canceled mid-way.
    private var currentAnimator: Animator? = null

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
        viewModel.getPlanet()
        viewModel.planetUIModel.observe(viewLifecycleOwner, Observer {
            onUiModelChanged(it)
        })

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
        buttonResidents.setOnClickListener {
            navigateToResidents(it, planetModel)
        }

        // Bind clicks on the thumbnail views.
        imagePlanet.setOnClickListener {
            zoomImageFromThumb(it as ImageView)
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

    /**
     * Set planet image zoom and animation
     * Ref: https://developer.android.com/training/animation/zoom
     * @param thumbView : Clicked image
     */
    private fun zoomImageFromThumb(thumbView: ImageView) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        currentAnimator?.cancel()

        // Load the high-resolution "zoomed-in" image.
        imageExpand.setImageDrawable(thumbView.drawable)

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        val startBoundsInt = Rect()
        val finalBoundsInt = Rect()
        val globalOffset = Point()

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBoundsInt)
        root.getGlobalVisibleRect(finalBoundsInt, globalOffset)
        startBoundsInt.offset(-globalOffset.x, -globalOffset.y)
        finalBoundsInt.offset(-globalOffset.x, -globalOffset.y)

        val startBounds = RectF(startBoundsInt)
        val finalBounds = RectF(finalBoundsInt)

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        val startScale: Float
        if ((finalBounds.width() / finalBounds.height() > startBounds.width() / startBounds.height())) {
            // Extend start bounds horizontally
            startScale = startBounds.height() / finalBounds.height()
            val startWidth: Float = startScale * finalBounds.width()
            val deltaWidth: Float = (startWidth - startBounds.width()) / 2
            startBounds.left -= deltaWidth.toInt()
            startBounds.right += deltaWidth.toInt()
        } else {
            // Extend start bounds vertically
            startScale = startBounds.width() / finalBounds.width()
            val startHeight: Float = startScale * finalBounds.height()
            val deltaHeight: Float = (startHeight - startBounds.height()) / 2f
            startBounds.top -= deltaHeight.toInt()
            startBounds.bottom += deltaHeight.toInt()
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.alpha = 0f
        imageExpand.makeVisible()

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        imageExpand.pivotX = 0f
        imageExpand.pivotY = 0f

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        currentAnimator = AnimatorSet().apply {
            play(
                ObjectAnimator.ofFloat(imageExpand, View.X, startBounds.left, finalBounds.left)
            ).apply {
                with(
                    ObjectAnimator.ofFloat(imageExpand, View.Y, startBounds.top, finalBounds.top)
                )
                with(ObjectAnimator.ofFloat(imageExpand, View.SCALE_X, startScale, 1f))
                with(ObjectAnimator.ofFloat(imageExpand, View.SCALE_Y, startScale, 1f))
            }
            duration = shortAnimationDuration.toLong()
            interpolator = DecelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator) {
                    currentAnimator = null
                }

                override fun onAnimationCancel(animation: Animator) {
                    currentAnimator = null
                }
            })
            start()
        }

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        imageExpand.setOnClickListener {
            currentAnimator?.cancel()

            // Animate the four positioning/sizing properties in parallel,
            // back to their original values.
            currentAnimator = AnimatorSet().apply {
                play(ObjectAnimator.ofFloat(imageExpand, View.X, startBounds.left)).apply {
                    with(ObjectAnimator.ofFloat(imageExpand, View.Y, startBounds.top))
                    with(ObjectAnimator.ofFloat(imageExpand, View.SCALE_X, startScale))
                    with(ObjectAnimator.ofFloat(imageExpand, View.SCALE_Y, startScale))
                }
                duration = shortAnimationDuration.toLong()
                interpolator = DecelerateInterpolator()
                addListener(object : AnimatorListenerAdapter() {

                    override fun onAnimationEnd(animation: Animator) {
                        thumbView.alpha = 1f
                        imageExpand.makeGone()
                        currentAnimator = null
                    }

                    override fun onAnimationCancel(animation: Animator) {
                        thumbView.alpha = 1f
                        imageExpand.makeGone()
                        currentAnimator = null
                    }
                })
                start()
            }
        }
    }
}