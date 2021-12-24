package hks.dev.play.labdroid.ui.home

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.*

import androidx.savedstate.SavedStateRegistryOwner
import hks.dev.play.labdroid.network.NetworkManager
import hks.dev.play.labdroid.network.NetworkService
import hks.dev.play.labdroid.network.PokemonRepository

class SavedStateHomeViewModelFactory(
    private val application: Application,
    private val owner: SavedStateRegistryOwner,
    private val repository: PokemonRepository
) : AbstractSavedStateViewModelFactory(owner, Bundle.EMPTY) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
       return SavedStateHomeViewModel(
            handle,
            application,
            repository
        ) as T
    }

}