package com.example.myweatherforecastapp.di

import com.example.data.repository.ForecastWeatherRepositoryImpl
import com.example.data.repository.KeyValueRepositoryImpl
import com.example.domain.repository.ForecastWeatherRepository
import com.example.domain.repository.KeyValueRepository
import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {

    @Binds
    fun provideMovieRepositoryImpl(impl: ForecastWeatherRepositoryImpl): ForecastWeatherRepository

    @Binds
    fun provideKeyValueRepositoryImpl(impl: KeyValueRepositoryImpl): KeyValueRepository
}