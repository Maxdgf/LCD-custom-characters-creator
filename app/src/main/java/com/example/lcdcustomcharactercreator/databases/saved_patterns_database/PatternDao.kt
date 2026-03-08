package com.example.lcdcustomcharactercreator.databases.saved_patterns_database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.example.lcdcustomcharactercreator.databases.saved_patterns_database.entities.SavedPatternEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PatternDao {
    @Query("SELECT * FROM saved_patterns")
    fun getAllSavedPatterns(): Flow<List<SavedPatternEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun addPattern(pattern: SavedPatternEntity)

    @Query("SELECT * FROM saved_patterns WHERE id = :id")
    fun getSavedPatternById(id: Int): Flow<SavedPatternEntity>

    @Query("DELETE FROM saved_patterns WHERE id = :id")
    suspend fun deletePatternById(id: Int)

    @Query("UPDATE saved_patterns SET pattern_source = :patternSource, source_code = :sourceCode, is_lcd_blue = :isLcdBlue WHERE id = :id")
    suspend fun updateExistingPattern(patternSource: String, sourceCode: String, id: Int, isLcdBlue: Boolean)

    @Query("DELETE FROM saved_patterns")
    suspend fun deleteAllSavedPatterns()
}