package com.example.pequenospasos.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun esHoy(fechaString: String): Boolean {
    return try {
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val fecha = LocalDateTime.parse(fechaString, formatter)
        val hoy = LocalDate.now()
        fecha.toLocalDate() == hoy
    } catch (e: Exception) {
        false
    }
}