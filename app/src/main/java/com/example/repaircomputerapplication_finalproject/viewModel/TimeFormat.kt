package com.example.repaircomputerapplication_finalproject.viewModel

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun formatTimestamp(timestamp: String?): String {
    return if (timestamp.isNullOrBlank()) {
        "Invalid date"
    } else {
        try {
            val zonedDateTime = ZonedDateTime.parse(timestamp, DateTimeFormatter.ISO_DATE_TIME)
            val formatter = DateTimeFormatter.ofPattern("d MMM yyyy, HH:mm", Locale("th", "TH"))
            zonedDateTime.format(formatter)
        } catch (e: Exception) {
            "Invalid date"
        }
    }
}