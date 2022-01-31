package com.example.domain.usecases

import com.example.domain.repository.KeyValueRepository

class SetAppFirstStartUseCase(private val repository: KeyValueRepository) {

    fun execute(key: String, value: Boolean) {
        repository.setBoolean(key, value)
    }
}