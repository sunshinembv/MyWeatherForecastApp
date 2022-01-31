package com.example.myweatherforecastapp.presentation.weather_forecast

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.myweatherforecastapp.BaseFragment
import com.example.myweatherforecastapp.NeedRationaleDialog
import com.example.myweatherforecastapp.R
import com.google.android.gms.location.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.Duration

class DefaultCityForecastWeatherFragment : BaseFragment(R.layout.fragment_forecast_weather) {

    private val fusedLocationProviderClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireContext())
    }

    private var locationRequest: LocationRequest = createLocationRequest()
    private var locationCallback: LocationCallback = createLocationCallback()

    private val locationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            when {
                granted -> getCurrentWeather(locationRequest, locationCallback)

                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION) ->
                    showRationaleDialog()

                else -> {
                    Toast.makeText(
                        requireContext(),
                        getText(R.string.impossible_get_current_location_without_permission),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.icon_search_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        findNavController().navigate(R.id.action_defaultCityForecastWeatherFragment_to_searchCityFragment)
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    fun requestLocationPermission() {
        locationPermission.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    private fun showRationaleDialog() {
        NeedRationaleDialog.newInstance(
            R.string.allow,
            R.string.reject,
            R.string.need_location_info_message
        ).show(childFragmentManager, NEED_RATIONALE_DIALOG)
    }

    private fun isPermissionGranted(): Boolean = ActivityCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    private fun createLocationRequest(): LocationRequest {
        return LocationRequest.create().apply {
            interval = Duration.ofSeconds(60).toMillis()
            fastestInterval = Duration.ofSeconds(30).toMillis()
            maxWaitTime = Duration.ofMinutes(2).toMillis()
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private fun createLocationCallback(): LocationCallback {
        return object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val lastLocation = locationResult.lastLocation
                forecastWeatherViewModel.getCurrentForecastWeather(
                    lastLocation.latitude,
                    lastLocation.longitude
                )
            }
        }
    }

    private fun getCurrentWeather(
        locationRequest: LocationRequest,
        locationCallback: LocationCallback
    ) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    private fun init() {
        lifecycleScope.launch {
            forecastWeatherViewModel.isAppFirstStart.flowWithLifecycle(
                lifecycle,
                Lifecycle.State.STARTED
            ).collect { isFirstStart ->
                if (isFirstStart) {
                    requestLocationPermission()
                    forecastWeatherViewModel.saveAppFirstStart(APP_FIRST_START_KEY, false)
                }
                if (isPermissionGranted()) {
                    getCurrentWeather(locationRequest, locationCallback)
                } else {
                    forecastWeatherViewModel.getCurrentForecastWeather(
                        LONDON_LATITUDE_DEFAULT,
                        LONDON_LONGITUDE_DEFAULT
                    )
                }
            }
        }
    }

    companion object {
        private const val NEED_RATIONALE_DIALOG = "needRationaleDialog"
        const val APP_FIRST_START_KEY = "appFirstStart"
        const val LONDON_LATITUDE_DEFAULT = 51.5073
        const val LONDON_LONGITUDE_DEFAULT = -0.1276
    }
}