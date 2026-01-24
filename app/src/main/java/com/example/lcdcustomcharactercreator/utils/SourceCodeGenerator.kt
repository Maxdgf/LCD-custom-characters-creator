package com.example.lcdcustomcharactercreator.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle
import java.util.BitSet

class SourceCodeGenerator {
    companion object {
        const val MAP_SIZE = 40
        const val MAP_WIDTH = 5
    }

    /**
     * Generates a cpp byte array which contains bytes for custom character.
     * @param pixelsMap pixels map.
     * @return source code as annotated string.
     */
    fun generateSourceCode(pixelsMap: BitSet): AnnotatedString {
        val output = AnnotatedString.Builder()
        output.apply {
            withStyle(SpanStyle(color = Color.Blue)) { append("byte") }
            withStyle(SpanStyle(color = Color.White)) { append(" my_character[") }
            withStyle(SpanStyle(color = Color.Cyan)) { append('8') }
            withStyle(SpanStyle(color = Color.White)) { append("] = {") }
        }

        val byteString = StringBuilder()
        for (i in 0..MAP_SIZE - 1) {
            if (i == 0 || i % MAP_WIDTH == 0) byteString.append("\n\t0b")
            val value = if (pixelsMap.get(i)) '1' else '0' // 1 or 0
            byteString.append(value)
        }

        output.apply {
            withStyle(SpanStyle(color = Color.Cyan)) { append(byteString) }
            withStyle(SpanStyle(color = Color.White)) { append("\n};") }
        }

        return output.toAnnotatedString()
    }
}