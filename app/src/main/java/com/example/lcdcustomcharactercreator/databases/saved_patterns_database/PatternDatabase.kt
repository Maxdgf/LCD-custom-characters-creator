package com.example.lcdcustomcharactercreator.databases.saved_patterns_database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.lcdcustomcharactercreator.databases.saved_patterns_database.entities.SavedPatternEntity

@Database(
    entities = [
        SavedPatternEntity::class
    ],
    version = 2
)
abstract class PatternDatabase : RoomDatabase() {
    abstract fun getPatternDao(): PatternDao
}