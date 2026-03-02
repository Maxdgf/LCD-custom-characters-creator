package com.example.lcdcustomcharactercreator.databases.saved_patterns_database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.lcdcustomcharactercreator.databases.saved_patterns_database.entities.PatternEntity
import com.example.lcdcustomcharactercreator.databases.saved_patterns_database.entities.SavedPatternEntity

@Database(
    entities = [
        PatternEntity::class,
        SavedPatternEntity::class
    ],
    version = 1
)
abstract class PatternDatabase : RoomDatabase() {
    abstract fun getPatternDao(): PatternDao
}