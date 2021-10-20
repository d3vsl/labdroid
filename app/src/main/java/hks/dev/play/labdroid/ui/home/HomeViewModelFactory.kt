package hks.dev.play.labdroid.ui.home

import android.app.Application
import androidx.lifecycle.ViewModel

import androidx.lifecycle.ViewModelProvider
import hks.dev.play.labdroid.network.PokemonRepository

class HomeViewModelFactory(
    private val application: Application,
    private val pokemonRepository: PokemonRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(application, pokemonRepository) as T
    }

}