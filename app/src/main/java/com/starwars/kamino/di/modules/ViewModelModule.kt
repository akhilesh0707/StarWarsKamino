package com.starwars.kamino.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.starwars.kamino.di.utils.ViewModelKey
import com.starwars.kamino.di.utils.ViewModelProviderFactory
import com.starwars.kamino.ui.planet.PlanetViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelProviderFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(PlanetViewModel::class)
    abstract fun bindPlanetViewModel(viewModel: PlanetViewModel): ViewModel

}