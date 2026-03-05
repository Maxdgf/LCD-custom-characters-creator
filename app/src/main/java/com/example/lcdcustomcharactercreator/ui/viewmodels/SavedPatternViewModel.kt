package com.example.lcdcustomcharactercreator.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lcdcustomcharactercreator.databases.saved_patterns_database.entities.SavedPatternEntity
import com.example.lcdcustomcharactercreator.databases.saved_patterns_database.repository.PatternRepository
import com.example.lcdcustomcharactercreator.utils.DatetimePicker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

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

    fun addPattern(
        name: String,
        description: String?,
        source: String,
        isLcdBlueState: Boolean
    ) {
        viewModelScope.launch {
            patternRepository.addPattern(
                SavedPatternEntity(
                    name = name,
                    description = description,
                    creationDate = datetimePicker.pickDateTimeNow(),
                    source = source,
                    isLcdBlueState = isLcdBlueState
                )
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