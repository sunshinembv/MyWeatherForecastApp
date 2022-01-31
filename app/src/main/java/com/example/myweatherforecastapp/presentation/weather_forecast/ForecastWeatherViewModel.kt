package com.example.myweatherforecastapp.presentation.weather_forecast

import androidx.lifecycle.*
import com.example.domain.usecases.*
import com.example.myweatherforecastapp.presentation.weather_forecast.DefaultCityForecastWeatherFragment.Companion.APP_FIRST_START_KEY
import com.example.myweatherforecastapp.presentation.weather_forecast.mapper.ForecastWeatherUIMapper
import com.example.myweatherforecastapp.presentation.weather_forecast.mapper.SearchCityMapper
import com.example.myweatherforecastapp.presentation.weather_forecast.ui_model.ForecastWeatherUI
import com.example.myweatherforecastapp.presentation.weather_forecast.ui_model.SearchCityItemUI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class ForecastWeatherViewModel(
    private val getDailyForecastWeatherUseCase: GetDailyForecastWeatherUseCase,
    private val getCoordinatesUseCase: GetCoordinatesUseCase,
    private val getLocationNameUseCase: GetLocationNameUseCase,
    private val getAppIsFirstStartUseCase: GetAppIsFirstStartUseCase,
    private val setAppFirstStartUseCase: SetAppFirstStartUseCase,
    private val forecastWeatherUIMapper: ForecastWeatherUIMapper,
    private val searchCityMapper: SearchCityMapper,
) : ViewModel() {

    private val _forecastWeather = MutableStateFlow<UIState<ForecastWeatherUI>>(UIState.Empty)
    val forecastWeather: StateFlow<UIState<ForecastWeatherUI>> = _forecastWeather.asStateFlow()

    private val _searchCity = MutableStateFlow<UIState<List<SearchCityItemUI>>>(UIState.Empty)
    val searchCity: StateFlow<UIState<List<SearchCityItemUI>>> = _searchCity.asStateFlow()

    private val _isAppFirstStart =
        MutableStateFlow(getAppIsFirstStartUseCase.execute(APP_FIRST_START_KEY, true))
    val isAppFirstStart: StateFlow<Boolean> = _isAppFirstStart.asStateFlow()

    private var job: Job? = null

    fun getCurrentForecastWeather(
        lat: Double,
        lon: Double,
        exclude: String = "minutely,hourly,alerts",
        units: String = "metric",
        limit: Int = 1
    ) {
        viewModelScope.launch {
            try {
                _forecastWeather.emit(UIState.IsLoading)
                val dailyWeather = getDailyForecastWeatherUseCase.execute(lat, lon, exclude, units)
                val locationName = getLocationNameUseCase.execute(lat, lon, limit)
                val forecastWeatherUI = forecastWeatherUIMapper.fromDailyForecastWeather(
                    dailyWeather,
                    locationName.first().name
                )
                _forecastWeather.emit(UIState.Success(forecastWeatherUI))
            } catch (t: Throwable) {
                Timber.d(t)
                _forecastWeather.emit(UIState.Error(t.toString()))
            }
        }
    }

    private fun getCoordinatesByLocationName(locationName: String, limit: Int = 5) {
        viewModelScope.launch {
            try {
                val coordinates = getCoordinatesUseCase.execute(locationName, limit)
                val searchCityItemUI = searchCityMapper.fromCoordinates(coordinates)
                _searchCity.emit(UIState.Success(searchCityItemUI))
            } catch (t: Throwable) {
                Timber.d(t)
                _searchCity.emit(UIState.Error(t.toString()))
            }
        }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    fun searchCity(queryFlow: Flow<String>) {
        job = queryFlow.filter { it.isNotEmpty() }.debounce(500).mapLatest { locationName ->
            getCoordinatesByLocationName(locationName)
        }.launchIn(viewModelScope)
    }

    fun saveAppFirstStart(key: String, value: Boolean) {
        setAppFirstStartUseCase.execute(key, value)
    }

    override fun onCleared() {
        super.onCleared()
        job = null
    }

    class Factory @Inject constructor(
        private val getDailyForecastWeatherUseCase: GetDailyForecastWeatherUseCase,
        private val getCoordinatesUseCase: GetCoordinatesUseCase,
        private val getLocationNameUseCase: GetLocationNameUseCase,
        private val getAppIsFirstStartUseCase: GetAppIsFirstStartUseCase,
        private val setAppFirstStartUseCase: SetAppFirstStartUseCase,
        private val forecastWeatherUIMapper: ForecastWeatherUIMapper,
        private val searchCityMapper: SearchCityMapper
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == ForecastWeatherViewModel::class.java)
            return ForecastWeatherViewModel(
                getDailyForecastWeatherUseCase,
                getCoordinatesUseCase,
                getLocationNameUseCase,
                getAppIsFirstStartUseCase,
                setAppFirstStartUseCase,
                forecastWeatherUIMapper,
                searchCityMapper
            ) as T
        }
    }
}