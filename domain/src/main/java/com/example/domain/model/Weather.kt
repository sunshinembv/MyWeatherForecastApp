package com.example.domain.model

interface Weather {
    val id: Int
    val main: String
    val description: String
    val icon: String
}