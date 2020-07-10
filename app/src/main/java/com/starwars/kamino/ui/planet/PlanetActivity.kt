package com.starwars.kamino.ui.planet

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.starwars.kamino.R
import com.starwars.kamino.base.BaseActivity
import com.starwars.kamino.utils.bindViewModel
import javax.inject.Inject

class PlanetActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by bindViewModel<PlanetViewModel>(lazy { viewModelFactory })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planet)
        viewModel.getPlanet()
    }
}
