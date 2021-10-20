package hks.dev.play.labdroid.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import hks.dev.play.labdroid.databinding.FragmentHomeBinding
import hks.dev.play.labdroid.network.NetworkManager
import hks.dev.play.labdroid.network.NetworkService
import hks.dev.play.labdroid.network.PokemonRepository
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.math.BigDecimal

class HomeFragment : Fragment() {
    private val TAG = "HomeFragment"

    private val homeViewModel by viewModels<HomeViewModel> {
        val networkService = NetworkManager
            .provideRetrofit(NetworkManager.provideOkHttpClient())
            .create(NetworkService::class.java)
        val repository = PokemonRepository(networkService)
        HomeViewModelFactory(
            application = activity?.application!!,
            pokemonRepository = repository
        )
    }
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        //start
        val coldFlow = flowOf(1, 2, 3, 4, 5)
            .map { it + 1 }
        lifecycleScope.launch {
            coldFlow.collect {
                Log.d(TAG, "collect1: $it")
            }
            coldFlow
                .onCompletion {
                    // flow can complete normally
                    Log.d(TAG, "collect2: DONE")
                }
                .collect {
                    Log.d(TAG, "collect2: $it")
                }
        }

        lifecycleScope.launchWhenStarted {
            homeViewModel.priceStateFlow
                .onCompletion {
                    //State flow cannot complete normally expect job is cancelled
                    binding.vStockPrice.text = "DONE!!"
                }
                .collect {
                    binding.vStockPrice.text = it.toString()
                }
        }
        lifecycleScope.launchWhenStarted {
            delay(1000)
            Log.d(TAG, "sharedFlow start collecting")
            homeViewModel.nameSharedFlow
                .onCompletion {
                    //Shared flow cannot complete normally expect job is cancelled
                    binding.vStockName.text = "DONE!!"
                }
                .collect {
                    binding.vStockName.text = it.toString()
                }
        }

//        val channel = BroadcastChannel<Int>(10)
//        channel.send(2)
//        channel.close()

        lifecycleScope.launchWhenStarted {
            val take2FromFlow: Flow<Any> =  flowFunction().take(2)
            Log.d(TAG, "flowFunction: take2FromFlow: ${take2FromFlow.toString()}")

            val first = take2FromFlow.collect()
            Log.d(TAG, "flowFunction: first:${first}")

            val second = take2FromFlow.collect()
            Log.d(TAG, "flowFunction: second:$second")

            val third = take2FromFlow.collect()
            Log.d(TAG, "flowFunction: third:$third")
        }
    }

    private fun flowFunction() = flow<String> {
        emit("A")
        delay(1000)
        emit("B")
        delay(1000)
        emit(BigDecimal(89).toString())
        delay(1000)
        emit("end of flow function")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}