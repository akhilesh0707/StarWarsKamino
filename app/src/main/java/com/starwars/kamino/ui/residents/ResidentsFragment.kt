package com.starwars.kamino.ui.residents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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

class ResidentsFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var requestManager: RequestManager
    private val viewModel by bindViewModel<ResidentsViewModel>(lazy { viewModelFactory })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_residents, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val planet: PlanetModel? = arguments?.getParcelable(PLANET_ARG_KEY) as? PlanetModel

        if (planet == null) {
            findNavController().popBackStack()
            return
        }
    }

    companion object {
        private const val PLANET_ARG_KEY = "planet"
    }

}