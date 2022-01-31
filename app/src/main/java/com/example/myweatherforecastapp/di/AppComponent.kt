package com.example.myweatherforecastapp.di

import android.content.Context
import com.example.data.di.NetworkModule
import com.example.myweatherforecastapp.BaseFragment
import com.example.myweatherforecastapp.presentation.weather_forecast.SearchCityFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [ForecastWeatherUseCaseModule::class, RepositoryModule::class, NetworkModule::class, SharedPreferencesModule::class])
@Singleton
interface AppComponent {
    fun inject(searchCityFragment: SearchCityFragment)
    fun inject(baseFragment: BaseFragment)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }
}