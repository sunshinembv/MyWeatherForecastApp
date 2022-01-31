package com.example.data.network

object Keys {

    init {
        System.loadLibrary("native-lib")
    }

    external fun apiKey(): String
}