package com.example.myweatherforecastapp.presentation.weather_forecast

sealed class UIState<out T> where T : Any? {
    object IsLoading : UIState<Nothing>()
    object Empty : UIState<Nothing>()
    data class Success<T>(val dataUI: T) : UIState<T>()
    data class Error(val message: String) : UIState<Nothing>()
}