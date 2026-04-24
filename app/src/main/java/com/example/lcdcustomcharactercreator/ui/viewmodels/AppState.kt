package com.example.lcdcustomcharactercreator.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.BitSet

class AppState : ViewModel() {
    companion object {
        const val MAP_SIZE = 40
    }

    private var isPixelsMapInvertedState = false // inverted pixels state

    /*
    * Pixels map as java BitSet.
    * set bits - pixel enabled or disabled if not set.
    * Exception: this behavior only changes when inverting pixels, unset bits are set and set bits are cleared.
    */
    private val _selectedPixelsMap = MutableStateFlow<BitSet>(BitSet(MAP_SIZE))
    val selectedPixelsMap = _selectedPixelsMap.asStateFlow()

    // ui states
    var lcdPreviewSkinState by mutableStateOf<Pair<Boolean, Boolean>>(Pair(true, false))
    var sourceCodeDialogState by mutableStateOf(false)

    private val _generatedSourceCodeState = MutableStateFlow<AnnotatedString>(AnnotatedString("")) // generated highlighted source code
    val generatedSourceCodeState = _generatedSourceCodeState.asStateFlow()

    private val _binaryOrHexType = MutableStateFlow<Pair<Boolean, Boolean>>(Pair(true, false)) // binary mode selected by default
    val binaryOrHexType = _binaryOrHexType.asStateFlow()
    private val _dataType = MutableStateFlow<String>("binary") // current data type
    val dataType = _dataType.asStateFlow()

    /**
     * Checks, is index in range.
     *
     * @param index current index.
     * @return bool state.
     */
    private fun isIndexExists(index: Int) = index >= 0 && index < MAP_SIZE

    /**
     * Selects data type and updates data type flags states by data type name.
     * @param dataTypeName data type name.
     */
    fun selectDataType(dataTypeName: String) {
        // binary or hexadecimal
        _binaryOrHexType.value = when (dataTypeName) {
            "binary" -> Pair(true, false) // binary enabled
            "hex" -> Pair(false, true) // hexadecimal enabled
            else -> Pair(true, false) // binary enabled (default)
        }
        _dataType.value = dataTypeName // set data type
    }

    /**
     * Updates pixels map. Enables or disables pixel on position by index and state.
     *
     * @param index bit position.
     * @param state state for bit(true - enable or false - disable).
     */
    fun updateSelectedPixelsMap(index: Int, state: Boolean) {
        if (isIndexExists(index)) {
            val editedMap = _selectedPixelsMap.value.clone() as BitSet // new empty bitset

            // manage pixel state
            if (state) editedMap.set(index) // set bit
            else editedMap.clear(index) // clear bit

            _selectedPixelsMap.value = editedMap // set new bitset to current
        }
    }

    /**
     * Checks if there are any pixels enabled.
     * @return bool state.
     */
    fun isPixelsSelected() = _selectedPixelsMap.value.cardinality() > 0

    /**Inverts pixels map.*/
    fun invertPixelsMap() {
        val editedMap = _selectedPixelsMap.value.clone() as BitSet // new empty bitset
        isPixelsMapInvertedState = !isPixelsMapInvertedState

        for (i in 0..MAP_SIZE - 1) {
            val pixel = editedMap.get(i)
            if (!isPixelsMapInvertedState) // invert
                if (pixel) editedMap.clear(i)
                else editedMap.set(i)
            else // return to default
                if (!pixel) editedMap.set(i)
                else editedMap.clear(i)
        }

        _selectedPixelsMap.value = editedMap // set new bitset to current
    }

    /**Clears all pixels in pixels map.*/
    fun clearSelectedPixelsMap() { _selectedPixelsMap.value = BitSet(MAP_SIZE) }

    /**Returns all active pixels in map.*/
    fun getActivePixels() = _selectedPixelsMap.value.cardinality()


    /**
     * Updates lcd display frame skin by color name.
     * @param color color name.
     */
    fun setLcdPreviewSkin(color: String) {
        lcdPreviewSkinState = when (color) {
            "blue" -> Pair(true, false) // set blue color
            "green" -> Pair(false, true) // set green color
            else -> Pair(true, false) // set blue color (default)
        }
    }

    /**
     * Updates source code dialog state.
     * @param state bool state.
     */
    fun updateSourceCodeDialogState(state: Boolean) { sourceCodeDialogState = state }

    /**
     * Sets generated code string to source code state.
     * @param code generated code annotated string.
     */
    fun setGeneratedSourceCode(code: AnnotatedString) { _generatedSourceCodeState.value = code }
}