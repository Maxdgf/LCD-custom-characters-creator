package com.example.lcdcustomcharactercreator.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lcdcustomcharactercreator.databases.saved_patterns_database.entities.SavedPatternEntity
import com.example.lcdcustomcharactercreator.databases.saved_patterns_database.repository.PatternRepository
import com.example.lcdcustomcharactercreator.utils.DatetimePicker
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class SavedPatternViewModel @Inject constructor(
    private val patternRepository: PatternRepository,
    private val datetimePicker: DatetimePicker
) : ViewModel() {
    private val _patternId = MutableStateFlow<Int?>(null)

    val allSavedPatterns = patternRepository.getAllSavedPatterns().stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        emptyList()
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val selectedPattern = _patternId
        .filterNotNull()
        .flatMapLatest { id ->
            patternRepository.getSavedPatternById(id)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            0
        )

    fun setPatternId(id: Int) { _patternId.value = id }

    fun addPattern(
        name: String,
        description: String?,
        source: String,
        isLcdBlueState: Boolean,
        sourceCode: String
    ) {
        viewModelScope.launch {
            patternRepository.addPattern(
                SavedPatternEntity(
                    name = name,
                    description = description,
                    creationDate = datetimePicker.pickDateTimeNow(),
                    source = source,
                    isLcdBlueState = isLcdBlueState,
                    sourceCode = sourceCode
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