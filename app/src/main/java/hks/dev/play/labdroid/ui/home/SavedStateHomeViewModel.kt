package hks.dev.play.labdroid.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import hks.dev.play.labdroid.network.NetworkManager
import hks.dev.play.labdroid.network.NetworkService
import hks.dev.play.labdroid.network.PokemonRepository
import hks.dev.play.labdroid.network.PokemonResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.create
import kotlin.random.Random

class SavedStateHomeViewModel(
    private val savedSateHandle: SavedStateHandle,
    private val app: Application,
    private val repository: PokemonRepository
) : AndroidViewModel(app) {
    private val TAG = "HomeViewModel"

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    //State Flow
    private val _priceStateFlow = MutableStateFlow(10)
    val priceStateFlow: StateFlow<Int> = _priceStateFlow

    //shared flow
    private val _nameSharedFlow = MutableSharedFlow<String>(
        replay = 1,
        extraBufferCapacity = 10
    )
    val nameSharedFlow: SharedFlow<String> = _nameSharedFlow

    init {
        viewModelScope.launch {
            while (true) {
                delay(500)
                _priceStateFlow.value = Random.nextInt(100)
            }
        }


        viewModelScope.launch {
            _nameSharedFlow.emit("TSM")
        }


        viewModelScope.launch {
           try {
                val response: PokemonResponse? = repository.getPokemonList().first()
                val name = response?.results?.first()?.name?:""
                _nameSharedFlow.emit(name)
                Log.d(TAG, "sharedFlow emitted $name")
            } catch (t: Throwable) {
                Log.d(TAG, "t: $t")
            }
        }
    }

}