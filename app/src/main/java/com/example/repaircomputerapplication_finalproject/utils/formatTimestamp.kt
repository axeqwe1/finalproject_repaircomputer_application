package com.example.repaircomputerapplication_finalproject.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.ZoneId
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
                .withZoneSameInstant(ZoneId.of("Asia/Bangkok")) // เปลี่ยน Time Zone
            val formatter = DateTimeFormatter.ofPattern("d/MMM/yyyy, HH:mm", Locale("th", "TH"))
            zonedDateTime.format(formatter)
        } catch (e: Exception) {
            "Invalid date"
        }
    }
}