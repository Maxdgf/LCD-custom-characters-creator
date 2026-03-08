package com.example.lcdcustomcharactercreator.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.example.lcdcustomcharactercreator.databases.saved_patterns_database.entities.SavedPatternEntity
import com.example.lcdcustomcharactercreator.databases.saved_patterns_database.repository.PatternRepository
import com.example.lcdcustomcharactercreator.utils.DatetimePicker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

@HiltViewModel
class SavedPatternViewModel @Inject constructor(
    private val patternRepository: PatternRepository,
    private val datetimePicker: DatetimePicker
) : ViewModel() {
    val allSavedPatterns = patternRepository.getAllSavedPatterns().stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        emptyList()
    )

    private val _openedPatternId = MutableStateFlow<Int?>(null)
    val openedPatternId = _openedPatternId.asStateFlow()


    fun setOpenedPatternId(id: Int?) {
        _openedPatternId.value = id
    }

    fun addPattern(
        name: String,
        description: String?,
        source: String,
        isLcdBlueState: Boolean,
        sourceCode: String,
        sourceCodeDataType: String
    ) {
        viewModelScope.launch {
            patternRepository.addPattern(
                SavedPatternEntity(
                    name = name,
                    description = description,
                    creationDate = datetimePicker.pickDateTimeNow(),
                    source = source,
                    isLcdBlueState = isLcdBlueState,
                    sourceCode = sourceCode,
                    dataType = sourceCodeDataType
                )
            )
        }
    }

    fun updateExistingPattern(
        patternSource: String,
        sourceCode: String,
        id: Int,
        isLcdBlue: Boolean
    ) {
        viewModelScope.launch {
            patternRepository.updateExistingPattern(
                patternSource,
                sourceCode,
                id,
                isLcdBlue
            )
        }
    }

    fun deletePatternById(id: Int) {
        viewModelScope.launch {
            patternRepository.deletePatternById(id)
        }
    }

    fun deleteAllPatterns() {
        viewModelScope.launch {
            patternRepository.deleteAllPatterns()
        }
    }
}