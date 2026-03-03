package com.example.lcdcustomcharactercreator.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lcdcustomcharactercreator.databases.saved_patterns_database.repository.PatternRepositoryImpl
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class SavedPatternViewModel @Inject constructor(private val patternRepositoryImpl: PatternRepositoryImpl) : ViewModel() {
    val allSavedPatterns = patternRepositoryImpl.getAllSavedPatterns().stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        emptyList()
    )
}