package com.starwars.kamino.ui.residents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.RequestManager
import com.starwars.kamino.R
import com.starwars.kamino.base.BaseFragment
import com.starwars.kamino.ui.planet.model.PlanetModel
import com.starwars.kamino.ui.residents.model.ResidentModel
import com.starwars.kamino.utils.bindViewModel
import com.starwars.kamino.utils.makeGone
import com.starwars.kamino.utils.makeVisible
import kotlinx.android.synthetic.main.fragment_residents.*
import timber.log.Timber
import javax.inject.Inject

class ResidentsFragment : BaseFragment(), ResidentsAdapter.OnResidentClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var requestManager: RequestManager
    private val viewModel by bindViewModel<ResidentsViewModel>(lazy { viewModelFactory })
    lateinit var residentsAdapter: ResidentsAdapter

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

        // Set planet model to viewmodel and call get resident API
        viewModel.planet = planet
        viewModel.getResident()
        viewModel.residentUIModel.observe(viewLifecycleOwner, Observer {
            onUiModelChanged(it)
        })

        // Bind recyclerview adapter
        bindResidentList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.residentUIModel.removeObservers(this)
    }

    /**
     * Bind resident adapter to recyclerview
     */
    private fun bindResidentList() {
        residentsAdapter = ResidentsAdapter(viewModel.residentList, requestManager, this)
        recyclerView.apply {
            setHasFixedSize(true)
            this.layoutManager = GridLayoutManager(context, 2)
            this.adapter = residentsAdapter
        }
    }

    /**
     * On resident click redirecting to detail fragment
     * @param selectedResident
     */
    override fun onClick(selectedResident: ResidentModel) {
        // Navigate to the ResidentDetailFragment using navController and safeArgs
        val action = ResidentsFragmentDirections.directionResidentDetail(selectedResident)
        findNavController().navigate(action)
    }

    /**
     * Handle live data changes (start progress, get result and API error)
     * @param uiModel
     */
    private fun onUiModelChanged(uiModel: ResidentUIModel) {
        if (uiModel.isRedelivered)
            return

        when (uiModel) {
            is ResidentUIModel.Loading -> {
                progressBar.makeVisible()
            }
            is ResidentUIModel.Success -> {
                progressBar.makeGone()
                uiModel.residentModel.let {
                    viewModel.residentList.add(it)
                    residentsAdapter.notifyItemInserted(viewModel.residentList.size)
                }
            }
            is ResidentUIModel.Error -> {
                progressBar.makeGone()
                Toast.makeText(this.context, uiModel.error, Toast.LENGTH_LONG).show()
                Timber.e(uiModel.error)
            }
        }
    }

    companion object {
        private const val PLANET_ARG_KEY = "planet"
    }

}