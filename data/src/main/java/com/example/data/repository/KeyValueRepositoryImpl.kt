package com.example.data.repository

import android.content.SharedPreferences
import com.example.domain.repository.KeyValueRepository
import javax.inject.Inject

class KeyValueRepositoryImpl @Inject constructor(
    private val sharedPrefs: SharedPreferences
) : KeyValueRepository {
    override fun setBoolean(key: String, value: Boolean) {
        sharedPrefs.edit().putBoolean(key, value).apply()
    }

    override fun getBoolean(key: String, def: Boolean): Boolean = sharedPrefs.getBoolean(key, def)
}