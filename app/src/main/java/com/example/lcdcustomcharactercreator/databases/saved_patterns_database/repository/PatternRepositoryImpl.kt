package com.example.lcdcustomcharactercreator.databases.saved_patterns_database.repository

import com.example.lcdcustomcharactercreator.databases.saved_patterns_database.PatternDao
import com.example.lcdcustomcharactercreator.databases.saved_patterns_database.entities.SavedPatternEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PatternRepositoryImpl @Inject constructor(private val patternDao: PatternDao) : PatternRepository {
    override fun getAllSavedPatterns(): Flow<List<SavedPatternEntity>> = patternDao.getAllSavedPatterns()

    override suspend fun addPattern(pattern: SavedPatternEntity) = patternDao.addPattern(pattern)

    override suspend fun deleteAllPatterns() = patternDao.deleteAllSavedPatterns()
}