package com.example.lcdcustomcharactercreator.databases.saved_patterns_database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_patterns")
data class SavedPatternEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "pattern_id") val patternId: String,
    @ColumnInfo(name = "pattern_name") val name: String,
    @ColumnInfo(name = "pattern_description") val description: String? = null,
    @ColumnInfo(name = "pattern_creation_date") val creationDate: String
)