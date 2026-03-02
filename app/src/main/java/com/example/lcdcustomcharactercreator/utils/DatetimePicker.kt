package com.example.lcdcustomcharactercreator.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DatetimePicker {
    /**
     * Picks datetime by pattern: `dd:MM:yyyy HH:mm:ss E`.
     * @return string datetime.
     */
    fun pickDateTimeNow(): String {
        val dateTimeFormatter = DateTimeFormatter.ofPattern("dd:MM:yyyy HH:mm:ss E")
        val currentDateTime: String = LocalDateTime.now().format(dateTimeFormatter)

        return currentDateTime
    }
}