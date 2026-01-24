package com.example.lcdcustomcharactercreator.utils

import android.content.Context
import android.widget.Toast

class Toaster(private val context: Context) {
    /**
     * Shows a toast message (long or short by time).
     * @param message toast message string.
     * @param isLong state that determines toast message display time.
     */
    fun showToast(
        message: String,
        isLong: Boolean = false
    ) {
        // show toast message (long or short by time)
        if (isLong)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        else
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}