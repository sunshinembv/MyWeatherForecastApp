package com.example.myweatherforecastapp.di

import com.example.domain.repository.ForecastWeatherRepository
import com.example.domain.repository.KeyValueRepository
import com.example.domain.usecases.*
import dagger.Module
import dagger.Provides

@Module
class ForecastWeatherUseCaseModule {

    @Provides
    fun provideGetDailyForecastWeatherUseCase(forecastWeatherRepository: ForecastWeatherRepository): GetDailyForecastWeatherUseCase {
        return GetDailyForecastWeatherUseCase(forecastWeatherRepository)
    }

    @Provides
    fun provideGetCoordinatesUseCase(forecastWeatherRepository: ForecastWeatherRepository): GetCoordinatesUseCase {
        return GetCoordinatesUseCase(forecastWeatherRepository)
    }

    @Provides
    fun provideGetLocationNameUseCase(forecastWeatherRepository: ForecastWeatherRepository): GetLocationNameUseCase {
        return GetLocationNameUseCase(forecastWeatherRepository)
    }

    @Provides
    fun provideGetAppIsFirstStartUseCase(keyValueRepository: KeyValueRepository): GetAppIsFirstStartUseCase {
        return GetAppIsFirstStartUseCase(keyValueRepository)
    }

    @Provides
    fun provideSetAppFirstStartUseCase(keyValueRepository: KeyValueRepository): SetAppFirstStartUseCase {
        return SetAppFirstStartUseCase(keyValueRepository)
    }
}