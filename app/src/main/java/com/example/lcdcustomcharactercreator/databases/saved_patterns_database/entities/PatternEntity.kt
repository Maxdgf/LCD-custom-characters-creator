package com.example.lcdcustomcharactercreator.databases.saved_patterns_database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "patterns_data")
data class PatternEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "pattern_id") val patternId: String,
    @ColumnInfo(name = "pattern_source") val source: String,
    @ColumnInfo(name = "is_lcd_blue") val isLcdBlueState: Boolean,
    @ColumnInfo(name = "pattern_source_code") val sourceCode: String
)