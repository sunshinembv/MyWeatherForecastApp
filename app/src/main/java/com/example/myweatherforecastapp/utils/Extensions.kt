package com.example.myweatherforecastapp.utils

import android.content.Context
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.example.myweatherforecastapp.MyApp
import com.example.myweatherforecastapp.di.AppComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

val Context.appComponent: AppComponent
    get() = when (this) {
        is MyApp -> appComponent
        else -> applicationContext.appComponent
    }

fun Long.toDate(patternStr: String): String {
    val formatter = DateTimeFormatter.ofPattern(patternStr)
        .withZone(ZoneId.systemDefault())
    return formatter.format(Instant.ofEpochSecond(this))
}

fun Int.pressureMbToMMhg(): Int {
    return (this / 1.333).toInt()
}

fun <T : Fragment> T.withArguments(args: Bundle.() -> Unit): T {
    return apply {
        arguments = Bundle().apply(args)
    }
}

@ExperimentalCoroutinesApi
fun SearchView.queryTextFlow(): Flow<String> {
    return callbackFlow {
        val queryTextListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                trySend(query.orEmpty())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                trySend(newText.orEmpty())
                return true
            }
        }
        this@queryTextFlow.setOnQueryTextListener(queryTextListener)
        awaitClose {
            this@queryTextFlow.setOnQueryTextListener(null)
        }
    }
}