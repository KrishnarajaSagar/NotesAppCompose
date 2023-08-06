package com.example.learning3.utilities

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

object UtilityFunctions {
    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDateAndTime(lastModified: Long): String {
        val instant = Instant.ofEpochMilli(lastModified)
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val dateFormatter = DateTimeFormatter.ofPattern("MMM dd", Locale.getDefault())

        val formattedDate = dateFormatter.format(localDateTime)

        return "$formattedDate"
    }
}