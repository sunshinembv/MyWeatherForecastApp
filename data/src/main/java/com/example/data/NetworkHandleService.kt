package com.example.data

import com.example.domain.Result
import okhttp3.ResponseBody
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class NetworkHandleService @Inject constructor() {

    suspend fun <T : Any> apiCall(call: suspend () -> Response<T>): Result<T> {
        var response: Response<T>? = null
        var body: T? = null
        var errorBody: ResponseBody? = null
        try {
            response = call.invoke()
            when {
                response.isSuccessful -> body = response.body()
                else -> errorBody = response.errorBody()
            }
        } catch (t: Throwable) {
            Timber.d(t)
        }

        return when {
            body != null -> Result.Success(body)
            errorBody != null -> {
                val errorString = errorBody.charStream().use {
                    it.readText()
                }
                Result.Error(errorString)
            }
            else -> Result.Error("Unknown error: ${response?.raw()?.message}")
        }
    }
}