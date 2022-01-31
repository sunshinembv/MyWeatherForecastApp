package com.example.myweatherforecastapp

import android.app.Application
import com.example.myweatherforecastapp.di.AppComponent
import com.example.myweatherforecastapp.di.DaggerAppComponent
import timber.log.Timber


class MyApp : Application() {

    private var _appComponent: AppComponent? = null

    internal val appComponent: AppComponent
        get() = checkNotNull(_appComponent)

    override fun onCreate() {
        super.onCreate()
        _appComponent = DaggerAppComponent.builder().context(this).build()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}