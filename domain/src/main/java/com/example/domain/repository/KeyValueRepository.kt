package com.example.domain.repository

interface KeyValueRepository {
    fun setBoolean(key: String, value: Boolean)

    fun getBoolean(key: String, def: Boolean): Boolean
}