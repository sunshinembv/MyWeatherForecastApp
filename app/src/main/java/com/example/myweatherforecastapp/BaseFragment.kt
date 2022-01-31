package com.example.myweatherforecastapp

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.myweatherforecastapp.databinding.FragmentForecastWeatherBinding
import com.example.myweatherforecastapp.presentation.weather_forecast.ForecastWeatherViewModel
import com.example.myweatherforecastapp.presentation.weather_forecast.UIState
import com.example.myweatherforecastapp.presentation.weather_forecast.adapter.DailyForecastWeatherAdapter
import com.example.myweatherforecastapp.presentation.weather_forecast.ui_model.ForecastWeatherUI
import com.example.myweatherforecastapp.utils.appComponent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

open class BaseFragment(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId) {

    private val viewBinding by viewBinding(FragmentForecastWeatherBinding::bind)

    @Inject
    lateinit var forecastWeatherViewModelFactory: ForecastWeatherViewModel.Factory

    val forecastWeatherViewModel: ForecastWeatherViewModel by activityViewModels {
        forecastWeatherViewModelFactory
    }

    private var dailyForecastWeatherAdapter: DailyForecastWeatherAdapter? = null

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        bindViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dailyForecastWeatherAdapter = null
    }

    private fun initRecyclerView() {
        dailyForecastWeatherAdapter = DailyForecastWeatherAdapter()
        with(viewBinding.dailyWeatherRecyclerView) {
            adapter = dailyForecastWeatherAdapter
            layoutManager = LinearLayoutManager(requireContext())

            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    private fun bindViewModel() {
        lifecycleScope.launch {
            forecastWeatherViewModel.forecastWeather.flowWithLifecycle(
                lifecycle,
                Lifecycle.State.STARTED
            ).collect { uiState ->
                when (uiState) {
                    is UIState.Success<ForecastWeatherUI> -> {
                        setData(uiState)
                        dailyForecastWeatherAdapter?.updateForecastWeatherList(uiState.dataUI.dailyDailyForecast)
                        viewVisibility(uiState)
                    }
                    is UIState.Error -> {
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

    private fun viewVisibility(uiState: UIState<ForecastWeatherUI>) {
        viewBinding.dailyWeatherRecyclerView.isVisible = uiState is UIState.Success
        viewBinding.progressBar.isVisible = uiState is UIState.IsLoading
        with(viewBinding.currentForecastWeatherItem) {
            weatherTemperature.isVisible = uiState is UIState.Success
            locationName.isVisible = uiState is UIState.Success
            weatherDescription.isVisible = uiState is UIState.Success
            weatherFeelsLike.isVisible = uiState is UIState.Success
            weatherWind.isVisible = uiState is UIState.Success
            weatherPressure.isVisible = uiState is UIState.Success
            weatherHumidity.isVisible = uiState is UIState.Success
            weatherIcon.isVisible = uiState is UIState.Success
            weatherWindIcon.isVisible = uiState is UIState.Success
            weatherPressureIcon.isVisible = uiState is UIState.Success
            weatherHumidityIcon.isVisible = uiState is UIState.Success
        }
    }

    private fun setData(uiState: UIState.Success<ForecastWeatherUI>) {
        with(viewBinding.currentForecastWeatherItem) {
            locationName.text = uiState.dataUI.locationName
            weatherTemperature.text = getString(R.string.temperature, uiState.dataUI.degrees)
            weatherDescription.text = getString(R.string.description, uiState.dataUI.description)
            weatherFeelsLike.text = getString(R.string.feels_like, uiState.dataUI.feelsLike)
            weatherWind.text = getString(R.string.wind_speed, uiState.dataUI.wind)
            weatherPressure.text = getString(R.string.pressure, uiState.dataUI.pressure)
            weatherHumidity.text = getString(R.string.humidity, uiState.dataUI.humidity)

            Glide.with(requireContext()).load(getString(R.string.icon_url, uiState.dataUI.icon))
                .error(R.drawable.ic_download_error)
                .placeholder(R.drawable.ic_downloading).into(weatherIcon)
        }
    }
}