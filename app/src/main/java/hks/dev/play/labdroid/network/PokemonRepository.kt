package hks.dev.play.labdroid.network

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PokemonRepository(private val networkService: NetworkService) {

    @ExperimentalCoroutinesApi
    fun getPokemonList(): Flow<PokemonResponse?> = callbackFlow {
        networkService.fetchPokemonList().enqueue(
            object : Callback<PokemonResponse> {
                override fun onResponse(
                    call: Call<PokemonResponse>,
                    response: Response<PokemonResponse>
                ) {
                    trySend(response.body())
                }

                override fun onFailure(
                    call: Call<PokemonResponse>,
                    t: Throwable
                ) {
                    close(t)
                }
            })

        awaitClose {
            networkService.fetchPokemonList().cancel()
        }
    }

}