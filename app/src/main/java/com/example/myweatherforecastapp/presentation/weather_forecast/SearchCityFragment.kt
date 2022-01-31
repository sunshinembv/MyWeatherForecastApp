package com.example.myweatherforecastapp.presentation.weather_forecast

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.myweatherforecastapp.R
import com.example.myweatherforecastapp.databinding.FragmentSearchCityBinding
import com.example.myweatherforecastapp.presentation.weather_forecast.adapter.SearchCityAdapter
import com.example.myweatherforecastapp.presentation.weather_forecast.ui_model.SearchCityItemUI
import com.example.myweatherforecastapp.utils.appComponent
import com.example.myweatherforecastapp.utils.queryTextFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchCityFragment : Fragment(R.layout.fragment_search_city) {

    private val viewBinding by viewBinding(FragmentSearchCityBinding::bind)

    @Inject
    lateinit var forecastWeatherViewModelFactory: ForecastWeatherViewModel.Factory

    private val forecastWeatherViewModel: ForecastWeatherViewModel by activityViewModels {
        forecastWeatherViewModelFactory
    }

    private var searchCityAdapter: SearchCityAdapter? = null
    private var searchView: SearchView? = null

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        bindViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchCityAdapter = null
        searchView = null
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        searchItem.expandActionView()

        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean = false

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                findNavController().popBackStack()
                return true
            }
        })

        searchView = searchItem.actionView as SearchView
        searchView?.let {
            forecastWeatherViewModel.searchCity(it.queryTextFlow())
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun initRecyclerView() {
        searchCityAdapter = SearchCityAdapter { latitude, longitude ->
            val bundle = CityForecastWeatherFragment.args(latitude, longitude)
            findNavController().navigate(
                R.id.action_searchCityFragment_to_cityForecastWeatherFragment,
                bundle
            )
        }
        with(viewBinding.searchCityRecyclerView) {
            adapter = searchCityAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun bindViewModel() {
        lifecycleScope.launch {
            forecastWeatherViewModel.searchCity.flowWithLifecycle(
                lifecycle,
                Lifecycle.State.STARTED
            ).collect { uiState ->
                when (uiState) {
                    is UIState.Success<List<SearchCityItemUI>> -> {
                        searchCityAdapter?.updateCityList(uiState.dataUI)
                        viewVisibility(uiState)
                    }
                    is UIState.Error -> {
                        searchCityAdapter?.updateCityList(emptyList())
                        Toast.makeText(requireContext(), uiState.message, Toast.LENGTH_LONG)
                            .show()
                        viewVisibility(uiState)
                    }
                    is UIState.IsLoading -> viewVisibility(uiState)
                    is UIState.Empty -> viewVisibility(uiState)
                }
            }
        }
    }

    private fun viewVisibility(uiState: UIState<List<SearchCityItemUI>>) {
        viewBinding.progressBar.isVisible = uiState is UIState.IsLoading
        viewBinding.searchCityRecyclerView.isVisible = uiState is UIState.Success
    }

}