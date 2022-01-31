package com.example.domain.usecases

import com.example.domain.repository.KeyValueRepository

class GetAppIsFirstStartUseCase(private val repository: KeyValueRepository) {

    fun execute(key: String, def: Boolean): Boolean {
        return repository.getBoolean(key, def)
    }
}