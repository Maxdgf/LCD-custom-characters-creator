package com.example.lcdcustomcharactercreator.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

class ClipBoardManager(context: Context) {
    private val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager // get clipboard manager

    /**
     * Sets text to clipboard.
     * @param text text string.
     * @param label clipboard item label.
     */
    fun setTextToClipboard(
        text: String,
        label: String = "LCD custom char code",
    ) {
        val clipData = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clipData)
    }
}