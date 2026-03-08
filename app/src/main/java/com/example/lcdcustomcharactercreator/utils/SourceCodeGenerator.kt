package com.example.lcdcustomcharactercreator.utils

import java.util.BitSet
import kotlin.text.toInt

class SourceCodeGenerator {
    companion object {
        const val MAP_SIZE = 40 // pixels count
        const val MAP_WIDTH = 5 // pixels in width
    }

    /**
     * Reads bitset and returns list of binary string numbers.
     * @return string list.
     */
    private fun BitSet.readBitSet(): List<String> {
        val bits = StringBuilder() // output string
        for (i in 0..MAP_SIZE - 1) {
            val value = if (this.get(i)) '1' else '0' // 1 or 0 by bit state
            bits.append(value) // append bit state
        }
        return bits.chunked(MAP_WIDTH) // slice bits string on chunks
    }

    /**
     * Formats binary string num to binary string num with `0b` prefix.
     * @return formatted binary string num.
     */
    private fun String.formatToBinary(): String = "0b$this"

    /**
     * Formats binary string num to hex string num with `0x` prefix.
     * @return hex string num.
     */
    private fun String.formatToHex(): String =
        // convert binary num to hexadecimal string num in uppercase with 0x prefix
        "0x${String.format("%02X", this.toInt(2))}"

    /**
     * Generates a cpp byte array code which contains bytes for custom character.
     * @param pixelsMap pixels map.
     * @param dataType type of byte array elements *(default type = `binary`, all types = `binary`, `hex`)*
     * @return source code as annotated string.
     */
    fun generateSourceCppByteArrayCode(
        pixelsMap: BitSet,
        dataType: String
    ): String {
        val output = StringBuilder() // output string

        // add start part
        output.apply {
            append("byte")
            append(" char[")
            append('8')
            append("] = {")
        }

        val data = pixelsMap.readBitSet() // read pixels map
        val elements = when (dataType) {
            "binary" -> data.map { it.formatToBinary() }
            "hex" -> data.map { it.formatToHex() }
            else -> data.map { it.formatToBinary() } // default
        }

        // add byte array elements
        elements.forEachIndexed { index, element ->
            output.apply {
                append("\n\t$element")
                append(
                    if (index < data.lastIndex) ","
                    else ""
                ) // add comma or not
            }
        }
        output.append("\n};") // add end part

        return output.toString()
    }
}