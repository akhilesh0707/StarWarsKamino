package com.starwars.kamino.ui.planet

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.starwars.kamino.R
import com.starwars.kamino.base.BaseActivity
import com.starwars.kamino.utils.bindViewModel
import kotlinx.android.synthetic.main.activity_planet.*
import timber.log.Timber
import javax.inject.Inject

class PlanetActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by bindViewModel<PlanetViewModel>(lazy { viewModelFactory })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planet)
        viewModel.getPlanet()
        viewModel.planetUIModel.observe(this, Observer {
            onUiModelChanged(it)
        })
    }

    private fun onUiModelChanged(uiModel: PlanetUIModel) {
        when (uiModel) {
            is PlanetUIModel.Loading -> {
                textView.visibility = View.GONE
            }
            is PlanetUIModel.Success -> {
                Timber.d(uiModel.planetModel.toString())
                textView.visibility = View.VISIBLE
                textView.text = uiModel.toString()
            }
            is PlanetUIModel.Error -> {
                Timber.e(uiModel.error)
                textView.visibility = View.VISIBLE
                textView.text = uiModel.error
            }
        }
    }
}
