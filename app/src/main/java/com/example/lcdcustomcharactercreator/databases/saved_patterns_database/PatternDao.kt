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

    @Query("DELETE FROM saved_patterns")
    suspend fun deleteAllSavedPatterns()
}