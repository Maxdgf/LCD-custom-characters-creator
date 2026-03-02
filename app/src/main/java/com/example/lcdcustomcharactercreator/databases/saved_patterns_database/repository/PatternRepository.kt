package com.example.lcdcustomcharactercreator.databases.saved_patterns_database.repository

import com.example.lcdcustomcharactercreator.databases.saved_patterns_database.entities.SavedPatternEntity
import kotlinx.coroutines.flow.Flow

interface PatternRepository {
    fun getAllSavedPatterns(): Flow<List<SavedPatternEntity>>
    suspend fun addPattern(pattern: SavedPatternEntity)
    suspend fun deleteAllPatterns()
}