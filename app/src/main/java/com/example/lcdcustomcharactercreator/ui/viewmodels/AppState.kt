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
    var isBlueDisplayState by mutableStateOf(true)
    var sourceCodeDialogState by mutableStateOf(false)
    var dropDownMenuState by mutableStateOf(false)
    var savePatternDialogState by mutableStateOf(false)
    var saveableNameOfPattern by mutableStateOf("")
    var saveableDescriptionOfPattern by mutableStateOf("")

    private val _generatedSourceCodeState = MutableStateFlow<String>("") // generated highlighted source code
    val generatedSourceCodeState = _generatedSourceCodeState.asStateFlow()

    private val _binaryOrHexType = MutableStateFlow<Pair<Boolean, Boolean>>(Pair(true, false)) // binary mode selected by default
    val binaryOrHexType = _binaryOrHexType.asStateFlow()
    private val _dataType = MutableStateFlow<String>("binary") // current data type
    val dataType = _dataType.asStateFlow()

    /**
     * Checks, is index in range.
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
        when (dataTypeName) {
            "binary" -> _binaryOrHexType.value = Pair(true, false) // binary enabled
            "hex" -> _binaryOrHexType.value = Pair(false, true) // hexadecimal enabled
        }

        _dataType.value = dataTypeName // set data type
    }

    /**
     * Sets new pixels map.
     * @param map bit set to set.
     */
    fun setPixelsMap(map: BitSet) { _selectedPixelsMap.value = map }

    /**
     * Updates pixels map. Enables or disables pixel on position by index and state.
     * @param index bit position.
     * @param state state for bit(true - enable or false - disable).
     */
    fun updateSelectedPixelsMap(index: Int, state: Boolean) {
        if (isIndexExists(index)) {
            val editedMap = _selectedPixelsMap.value.clone() as BitSet // new empty bitset
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
     * Updates lcd display frame skin by state.
     * @param state bool state.
     */
    fun updateIsBlueDisplayState(state: Boolean) { isBlueDisplayState = state }

    /**
     * Updates source code dialog state.
     * @param state bool state.
     */
    fun updateSourceCodeDialogState(state: Boolean) { sourceCodeDialogState = state }

    /**
     * Sets generated code string to source code state.
     * @param code generated code annotated string.
     */
    fun setGeneratedSourceCode(code: String) { _generatedSourceCodeState.value = code }

    /**
     * Updates dropdown menu state.
     * @param state bool state.
     */
    fun updateDropDownMenuState(state: Boolean) { dropDownMenuState = state }

    /**
     * Updates save pattern dialog state.
     * @param state bool state.
     */
    fun updateSavePatternDialogState(state: Boolean) { savePatternDialogState = state }

    /**
     * Updates saveable name of pattern.
     * @param text pattern name.
     */
    fun updateSaveableNameOfPatternState(text: String) { saveableNameOfPattern = text }

    /**
     * Updates saveable description of pattern.
     * @param text pattern description.
     */
    fun updateSaveableDescriptionOfPattern(text: String) { saveableDescriptionOfPattern = text }
}